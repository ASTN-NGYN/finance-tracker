package com.austin.financetracker.security;

import java.io.IOException;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.austin.financetracker.service.FirebaseAuthService;
import com.austin.financetracker.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Spring Security filter that validates Firebase ID tokens for incoming
 * requests.
 * <p>
 * It extracts the token from the "Authorization" header, decodes it to get the
 * UID and email,
 * ensures the user exists in the database, attaches the UID as a request
 * attribute,
 * and continues the filter chain. Returns 401 if token is missing, invalid, or
 * expired.
 * <p>
 * Paths in EXCLUDE_URLS and OPTIONS requests are skipped.
 */
@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final FirebaseAuthService firebaseAuthService;
    private final UserService userService;

    private static final Set<String> EXCLUDE_URLS = Set.of("/public", "/login");

    public FirebaseAuthenticationFilter(FirebaseAuthService firebaseAuthService, UserService userService) {
        this.firebaseAuthService = firebaseAuthService;
        this.userService = userService;
    }

    /**
     * Determines if the filter should skip a request.
     * <p>
     * Skips OPTIONS requests and paths in EXCLUDE_URLS.
     *
     * @param request the incoming HTTP request
     * @return true if the filter should not run, false otherwise
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()))
            return true;
        return EXCLUDE_URLS.stream().anyMatch(path::startsWith);
    }

    /**
     * Validates the Firebase token, ensures the user exists, and attaches the UID
     * to the request.
     * <p>
     * Returns 401 if the Authorization header is missing, malformed, or token is
     * invalid/expired.
     *
     * @param request     incoming HTTP request
     * @param response    outgoing HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = header.substring(7).trim();
        try {
            var decodedToken = firebaseAuthService.decodeToken(token);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            userService.ensureUserExists(uid, email);
            request.setAttribute("uid", uid);

            filterChain.doFilter(request, response);
        } catch (FirebaseAuthException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
        }
    }
}
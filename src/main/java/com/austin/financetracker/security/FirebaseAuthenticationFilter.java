package com.austin.financetracker.security;

import com.austin.financetracker.service.FirebaseAuthService;
import com.google.firebase.auth.FirebaseAuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/**
 * A Spring Security filter that verifies Firebase ID tokens for incoming HTTP requests.
 * <p>
 * This filter intercepts every request (except those explicitly excluded) and:
 * <ul>
 *   <li>Extracts the Firebase ID token from the "Authorization" header</li>
 *   <li>Verifies the token using {@link FirebaseAuthService}</li>
 *   <li>If valid, attaches the user's Firebase UID as a request attribute named "uid"</li>
 *   <li>If invalid or missing, returns HTTP 401 Unauthorized</li>
 * </ul>
 * <p>
 * Endpoints can access the UID in controllers via:
 * <pre>{@code
 * String userUid = (String) request.getAttribute("uid");
 * }</pre>
 * <p>
 * Certain URLs (e.g., login or public endpoints) are excluded from filtering.
 */
@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final FirebaseAuthService firebaseAuthService;

    /**
     * List of URL paths to exclude from Firebase authentication.
     * <p>
     * Any request whose path starts with one of these values will skip the filter.
     */
    private static final Set<String> EXCLUDE_URLS = Set.of(
            "/public", "/login"
    );

    /**
     * Constructs a new {@code FirebaseAuthenticationFilter} with the given
     * {@link FirebaseAuthService}.
     *
     * @param firebaseAuthService the service used to verify Firebase ID tokens
     */
    public FirebaseAuthenticationFilter(FirebaseAuthService firebaseAuthService) {
        this.firebaseAuthService = firebaseAuthService;
    }

    /**
     * Determines if the filter should be skipped for a given request.
     * <p>
     * The filter is skipped if:
     * <ul>
     *   <li>The request method is OPTIONS (CORS preflight)</li>
     *   <li>The request path matches any URL in {@link #EXCLUDE_URLS}</li>
     * </ul>
     *
     * @param request the incoming HTTP request
     * @return {@code true} if the request should not be filtered; {@code false} otherwise
     * @throws ServletException if an error occurs during request processing
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        return EXCLUDE_URLS.stream().anyMatch(path::startsWith);
    }

    /**
     * Filters incoming HTTP requests by validating the Firebase ID token.
     * <p>
     * Steps:
     * <ol>
     *   <li>Extract the "Authorization" header and verify it starts with "Bearer "</li>
     *   <li>Extract the raw token from the header</li>
     *   <li>Verify the token using {@link FirebaseAuthService#verifyToken(String)}</li>
     *   <li>If valid, attach the UID as a request attribute "uid" and continue the filter chain</li>
     *   <li>If invalid or missing, respond with 401 Unauthorized</li>
     * </ol>
     *
     * @param request     the incoming HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain to continue processing if valid
     * @throws ServletException if an error occurs during request processing
     * @throws IOException      if an I/O error occurs during request processing
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
            String uid = firebaseAuthService.verifyToken(token); // can throw FirebaseAuthException
            request.setAttribute("uid", uid); // make UID available to controllers
            filterChain.doFilter(request, response);
        } catch (FirebaseAuthException e) {
            // Token invalid/expired — return 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
        }
    }
}
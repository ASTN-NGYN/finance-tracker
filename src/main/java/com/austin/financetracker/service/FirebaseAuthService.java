package com.austin.financetracker.service;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@Service
public class FirebaseAuthService {

    /**
     * Verifies the Firebase ID token and returns the associated user UID.
     *
     * @param idToken the Firebase ID token sent by the client
     * @return the Firebase UID
     * @throws FirebaseAuthException if the token is invalid or expired
     */
    public String verifyToken(String idToken) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        return decodedToken.getUid();
    }

    /**
     * Decodes the Firebase ID token and returns the full FirebaseToken object.
     * This lets you access the email or custom claims.
     *
     * @param idToken the Firebase ID token sent by the client
     * @return the decoded FirebaseToken
     * @throws FirebaseAuthException if the token is invalid or expired
     */
    public FirebaseToken decodeToken(String idToken) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }
}

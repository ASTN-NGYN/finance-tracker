package com.austin.financetracker.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

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
}

package com.austin.financetracker.config;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() throws IOException {
        String firebasePath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (firebasePath == null) {
            throw new IllegalStateException("Environment variable GOOGLE_APPLICATION_CREDENTIALS not set");
        }

        FileInputStream serviceAccount = new FileInputStream(firebasePath);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}

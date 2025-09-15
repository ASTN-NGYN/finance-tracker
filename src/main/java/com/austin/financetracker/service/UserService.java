package com.austin.financetracker.service;

import org.springframework.stereotype.Service;

import com.austin.financetracker.entity.User;
import com.austin.financetracker.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Ensures that a user with the given UID exists in the database.
     * If not found, creates and saves a new User.
     *
     * @param uid   the Firebase UID
     * @param email optional email from Firebase
     * @return the existing or newly created User
     */
    public User ensureUserExists(String uid, String email) {
        return userRepository.findByUid(uid)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUid(uid);
                    newUser.setEmail(email);
                    return userRepository.save(newUser);
                });
    }
}
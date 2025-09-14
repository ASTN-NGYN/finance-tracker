package com.austin.financetracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.austin.financetracker.entity.User;

/**
 * Repository interface for {@link User} entities.
 * Provides CRUD operations and the ability to find users by their UID.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Finds a {@link User} entity by its unique identifier (UID).
     *
     * @param uid the unique identifier of the user
     * @return an {@link Optional} containing the {@link User} if found,
     *         or an empty {@link Optional} if no user exists with the given UID
     */
    Optional<User> findByUserUid(String uid);
}

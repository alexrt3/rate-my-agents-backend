package com.ratemyleaser.rate_my_leaser_backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ratemyleaser.rate_my_leaser_backend.models.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(UUID id);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}

package com.ratemyleaser.rate_my_leaser_backend.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Column(name = "phonenumber", unique = true, length = 20)
    private String phoneNumber;

    @Column(name = "username", length = 255, unique = true, nullable = false)
    private String userName;

    @Column(name = "is_agent", nullable = false)
    private boolean isAgent;

    @Column(name = "created_at", nullable = false)
    @GeneratedValue(generator = "localDateTime")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
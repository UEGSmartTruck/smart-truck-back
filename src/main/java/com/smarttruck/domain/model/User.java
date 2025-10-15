package com.smarttruck.domain.model;

import java.time.Instant;
import java.util.UUID;

public class User {
    private final String id;
    private final String name;
    private final String email;
    private final String passwordHash;
    private final String phone;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;
    private final Instant loginAt;

    public User(String name, String phone, String email, String passwordHash) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.deletedAt = null;
        this.loginAt = null;
    }

    public User(String id, String name, String phone, String email, String passwordHash,
                Instant createdAt, Instant updatedAt, Instant deletedAt, Instant loginAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.loginAt = loginAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public Instant getLoginAt() {
        return loginAt;
    }
}

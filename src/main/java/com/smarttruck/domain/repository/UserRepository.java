public User updateUser(String id, String name, String email, String password, String phone) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    // Atualiza os campos
    user.setName(name);
    user.setEmail(email);
    user.setPhone(phone);
    user.setPasswordHash(passwordEncoder.encode(password));
    user.setUpdatedAt(Instant.now());

    return userRepository.save(user);
}

public void deleteUser(String id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
    user.setDeletedAt(Instant.now());
    userRepository.save(user);
}


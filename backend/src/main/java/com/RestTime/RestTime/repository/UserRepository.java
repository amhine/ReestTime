package com.RestTime.RestTime.repository;

import com.RestTime.RestTime.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
    java.util.List<User> findByRole(com.RestTime.RestTime.model.enumeration.Role role);
}

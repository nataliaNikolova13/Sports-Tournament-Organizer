package com.fmi.sporttournament.user.repository;

import com.fmi.sporttournament.user.entity.User;

import com.fmi.sporttournament.user.entity.role.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositoty extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByFullName(String fullName);
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> getByRole(@Param("role") Role role);
    @Query("SELECT u FROM User u")
    List<User> findAll();
}

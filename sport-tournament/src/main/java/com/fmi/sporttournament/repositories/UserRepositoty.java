package com.fmi.sporttournament.repositories;
import com.fmi.sporttournament.entity.User;

import com.fmi.sporttournament.entity.enums.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositoty extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> getByRole(@Param("role") Role role);
    @Query("SELECT u FROM User u")
    List<User> findAll();
}

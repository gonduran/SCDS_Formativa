package com.demo.backend_recetas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.backend_recetas.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    List<User> findByUserType(Integer userType);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
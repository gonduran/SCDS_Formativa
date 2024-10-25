package com.example.backend_recetas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.backend_recetas.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}

package com.example.jwtdemo3.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwtdemo3.entity.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByName(String username);

    Optional<User> findByEmail(String email);

}

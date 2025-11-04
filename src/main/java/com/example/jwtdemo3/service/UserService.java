package com.example.jwtdemo3.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jwtdemo3.entity.User;
import com.example.jwtdemo3.repo.UserRepo;

@Service
public class UserService {

  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
  }

  public String printToConsole() {
    return "This is PlaceHolder";
  }

  public String signupUser(User user) {

    if (!user.getPassword().equals(user.getConfirmPassword())) {
      return "Password and Confirm Password do not match";
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    user.setConfirmPassword(null);

    userRepo.save(user);
    return "User registered successfully";

  }

  public User signupIfNotExist(String email, String defaultName, String role) {
    Optional<User> existingUser = userRepo.findByEmail(email);
    if (existingUser.isPresent()) {
      return existingUser.get();
    }

    User user = new User();
    user.setEmail(email);
    user.setName(defaultName != null ? defaultName : email); // default name fallback
    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // random password
    user.setRoles(role); // e.g., "User"

    return userRepo.save(user);
  }

}

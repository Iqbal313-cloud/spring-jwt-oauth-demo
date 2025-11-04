package com.example.jwtdemo3.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.jwtdemo3.entity.User;
import com.example.jwtdemo3.repo.UserRepo;

@Component
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepo repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = repository.findByName(username);

    return user.map(CustomUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException("username not found" + username));
  }

}

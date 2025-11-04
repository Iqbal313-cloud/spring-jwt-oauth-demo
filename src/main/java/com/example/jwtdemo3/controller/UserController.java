package com.example.jwtdemo3.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwtdemo3.dto.AuthRequest;
import com.example.jwtdemo3.entity.User;
import com.example.jwtdemo3.service.JwtService;
import com.example.jwtdemo3.service.UserService;

@RestController
@RequestMapping("/user/v1")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @GetMapping("/welcome")
  @PreAuthorize("hasAnyAuthority('User','Admin')")
  public String welcome() {
    return "This Can be accessed if u are an Admin and User";
  }

  @GetMapping("/useronly")
  @PreAuthorize("hasAuthority('User')")
  public String useronly() {
    return "This Can be accessed if u are an User";
  }

  @GetMapping("/adminonly")
  @PreAuthorize("hasAuthority('Admin')")
  public String adminonly() {
    return "This Can be accessed if u are an Admin";
  }

  @PostMapping("/signup")
  public String addNewUser(@RequestBody User userEntity) {
    return userService.signupUser(userEntity);
  }

  @PostMapping("/login")
  public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

    if (authentication.isAuthenticated()) {
      return jwtService.generateToken(authRequest.getUsername());
    } else {
      throw new UsernameNotFoundException("Invalid User Request !");
    }
  }

  @GetMapping("/success")
  public ResponseEntity<?> oauth2Success(Authentication authentication) {
    OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
    String email = oauthUser.getAttribute("email");
    String name = oauthUser.getAttribute("name"); // optional

    // create or load user
    User user = userService.signupIfNotExist(email, name, "User");

    // generate JWT
    String token = jwtService.generateToken(user.getName());

    // return email and name
    return ResponseEntity.ok(Map.of(
        "email", user.getEmail(),
        "name", user.getName(),
        "jwt", token));
  }
}

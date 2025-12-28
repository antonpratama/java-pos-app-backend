package com.anton.learning.pos.service;

import com.anton.learning.pos.dto.AuthRequest;
import com.anton.learning.pos.dto.RegisterRequest;
import com.anton.learning.pos.entity.User;
import com.anton.learning.pos.repository.UserRepository;
import com.anton.learning.pos.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtUtil jwtUtil;

  public void register(RegisterRequest request){
    if (userRepository.existsByUsername(request.getUsername())){
      throw new RuntimeException("username already exists");
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    user.setRole(request.getRole());

    userRepository.save(user);
  }

  public String login(AuthRequest request){
    User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())){
      throw new RuntimeException("Invalid password");
    }

    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("role", user.getRole());

    return jwtUtil.generateToken(extraClaims, request.getUsername());
  }

  public User getCurrentUser(){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    String username = auth.getName();

    return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}

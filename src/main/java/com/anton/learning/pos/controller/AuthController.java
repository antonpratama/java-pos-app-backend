package com.anton.learning.pos.controller;

import com.anton.learning.pos.dto.AuthRequest;
import com.anton.learning.pos.dto.AuthResponse;
import com.anton.learning.pos.dto.RegisterRequest;
import com.anton.learning.pos.dto.WebResponse;
import com.anton.learning.pos.entity.User;
import com.anton.learning.pos.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService service;

  @PostMapping("/register")
  public ResponseEntity<WebResponse<Void>> register(@RequestBody RegisterRequest request){
    service.register(request);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/login")
  public ResponseEntity<WebResponse<AuthResponse>> login(@RequestBody AuthRequest request){
    String token = service.login(request);

    return ResponseEntity.ok().body(WebResponse.<AuthResponse>builder()
                    .data(new AuthResponse(token))
                    .httpStatus(HttpStatus.OK)
            .build());
  }

  @GetMapping("/me")
  public ResponseEntity<WebResponse<User>> me(){
    User currentUser = service.getCurrentUser();

    return ResponseEntity.ok().body(WebResponse.<User>builder()
                    .data(currentUser)
                    .httpStatus(HttpStatus.OK)
            .build());
  }
}

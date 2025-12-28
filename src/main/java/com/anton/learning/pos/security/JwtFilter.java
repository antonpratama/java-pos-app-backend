package com.anton.learning.pos.security;

import com.anton.learning.pos.service.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private CustomUserDetailService userDetailService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
      final String authHeader = request.getHeader("Authorization");
      final String token;
      final String username;
      final String role;

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
      }

      token = authHeader.substring(7);
      try {
        username = jwtUtil.extractUsername(token);
        role = jwtUtil.extractRole(token);
      } catch (Exception e) {
        filterChain.doFilter(request, response);
        return;
      }

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        if (jwtUtil.isTokenValid(token, userDetails)) {
          List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_"+role));
          UsernamePasswordAuthenticationToken auth =
                  new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }

      filterChain.doFilter(request, response);
  }
}

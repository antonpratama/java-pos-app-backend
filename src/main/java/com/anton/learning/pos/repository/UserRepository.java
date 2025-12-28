package com.anton.learning.pos.repository;

import com.anton.learning.pos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByUsername(String username);
  boolean existsByUsername(String username);
}

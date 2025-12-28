package com.anton.learning.pos.repository;

import com.anton.learning.pos.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

  Optional<Product> findBySku(String sku);
  List<Product> findByCategory_IdAndIsActiveTrue(String id);
  List<Product> findAllByIsActiveTrue();

  Page<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);
}

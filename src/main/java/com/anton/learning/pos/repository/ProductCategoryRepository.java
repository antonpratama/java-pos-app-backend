package com.anton.learning.pos.repository;

import com.anton.learning.pos.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {
  boolean existsByName(String name);

  List<ProductCategory> findAllByIsActiveTrue();
}

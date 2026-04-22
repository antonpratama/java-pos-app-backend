package com.anton.learning.pos.repository;

import com.anton.learning.pos.entity.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, String> {
  List<ProductImages> findByProduct_idAndIsActiveTrue(String productId);
  List<ProductImages> findByProduct_idAndIsActiveTrueAndImageIndex(String productId, int imageIndex);
  Long countByProduct_IdAndIsActiveTrue(String productId);
}

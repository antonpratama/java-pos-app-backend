package com.anton.learning.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

  private String id;
  private String name;
  private String description;
  private String sku;
  private BigDecimal price;
  private Integer stock;
  private boolean isActive;
  private ProductCategoryResponse category;
  private List<ProductImagesResponse> images;
}

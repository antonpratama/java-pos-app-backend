package com.anton.learning.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCategoryResponse {
  private String id;
  private String name;
  private String description;
  private boolean isActive;
}

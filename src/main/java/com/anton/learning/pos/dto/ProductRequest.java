package com.anton.learning.pos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

  @NotBlank
  private String name;

  private String description;

  @NotBlank
  private String sku;

  @NotNull
  private BigDecimal price;

  private Integer stock;

  private String categoryId;
}

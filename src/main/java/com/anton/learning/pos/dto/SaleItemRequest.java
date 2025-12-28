package com.anton.learning.pos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemRequest {

  @NotNull
  private String productId;

  @NotNull
  @Min(1)
  private Integer quantity;

}

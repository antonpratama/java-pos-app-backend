package com.anton.learning.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleItemResponse {

  private String id;

  private String productId;

  private String productName;

  private Integer quantity;

  private BigDecimal price;

  private BigDecimal subtotal;
}

package com.anton.learning.pos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleRequest {

  @NotNull
  private String cashierId;

  @NotBlank
  private String paymentType;

  @NotNull
  @DecimalMin(value = "0.0", inclusive = false)
  private BigDecimal paidAmount;

  @NotEmpty
  @Valid
  private List<SaleItemRequest> saleItems;
}

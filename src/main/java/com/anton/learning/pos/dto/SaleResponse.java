package com.anton.learning.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleResponse {

  private String id;

  private String invoiceNumber;

  private String cashierId;

  private String cashierName;

  private String paymentType;

  private BigDecimal totalAmount;

  private BigDecimal paidAmount;

  private BigDecimal changeAmount;

  private LocalDateTime createdAt;

  private List<SaleItemResponse> saleItems;
}

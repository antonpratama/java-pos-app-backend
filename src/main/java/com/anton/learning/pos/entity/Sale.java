package com.anton.learning.pos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sale {

  @Id
  @UuidGenerator
  private String id;

  @ManyToOne
  @JoinColumn(name = "cashier_id")
  private User cashier;

  private String invoiceNumber;

  private BigDecimal totalAmount;

  private BigDecimal paidAmount;

  private BigDecimal changeAmount;

  private String paymentType;

  private Boolean isActive = true;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
  private List<SaleItem> items;
}

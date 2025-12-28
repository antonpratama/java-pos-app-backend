package com.anton.learning.pos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleItem {

  @Id
  @UuidGenerator
  private String id;

  @ManyToOne
  @JoinColumn(name = "sale_id")
  private Sale sale;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  private Integer quantity;

  private BigDecimal price;

  private BigDecimal subtotal;
}

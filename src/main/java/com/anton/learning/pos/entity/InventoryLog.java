package com.anton.learning.pos.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table
public class InventoryLog {

  @Id
  @UuidGenerator
  private String id;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  private String changeType;

  private Integer quantity;

  private String reference;

  @CreationTimestamp
  private LocalDateTime createdAt;
}

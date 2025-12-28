package com.anton.learning.pos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

  @Id
  @UuidGenerator
  private String id;

  @Column(nullable = false)
  private String name;

  @Column(unique = true)
  private String sku;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private ProductCategory category;

  private Integer stock;

  private BigDecimal price;

  private BigDecimal costPrice;

  private String description;

  @Column(name = "is_active")
  private Boolean isActive = true;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
}

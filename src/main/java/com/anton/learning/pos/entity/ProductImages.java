package com.anton.learning.pos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_images")
@NoArgsConstructor
@Data
public class ProductImages {

  @Id
  @UuidGenerator
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Lob
  @Column(name = "image_data", nullable = false)
  private byte[] imageData;

  @Column(name = "mime_type", nullable = false)
  private String mimeType;

  @Column(name = "image_index")
  private Integer imageIndex;

  @Column(name = "is_active")
  private Boolean isActive = true;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
}

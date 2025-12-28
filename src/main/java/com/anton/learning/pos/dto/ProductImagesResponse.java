package com.anton.learning.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImagesResponse {

  private String id;

  private String mimeType;

  private Integer imageIndex;

  private String base64Image;

  private LocalDateTime createdAt;
}

package com.anton.learning.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImagesRequest {
  private Integer imageIndex;

  private String mimeType;
}

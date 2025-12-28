package com.anton.learning.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {

  private int currentPage;

  private int totalPage;

  private long totalElements;

  private int size;
}

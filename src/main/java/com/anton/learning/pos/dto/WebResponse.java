package com.anton.learning.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {

  private T data;

  private HttpStatus httpStatus;

  private String errors;

  private PagingResponse paging;
}

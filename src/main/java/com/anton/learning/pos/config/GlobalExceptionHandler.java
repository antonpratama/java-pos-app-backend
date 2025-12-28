package com.anton.learning.pos.config;

import com.anton.learning.pos.dto.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<WebResponse<Object>> handleResponseStatusException(ResponseStatusException ex) {
    return ResponseEntity.status(ex.getStatusCode()).body(
            WebResponse.builder()
                    .httpStatus(HttpStatus.valueOf(ex.getStatusCode().value()))
                    .errors(ex.getReason())
                    .data(null)
                    .paging(null)
                    .build()
    );
  }

//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<WebResponse<Object>> handleGenericException(Exception ex) {
//    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//            WebResponse.builder()
//                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .errors(ex.getMessage())
//                    .data(null)
//                    .paging(null)
//                    .build()
//    );
//  }
}

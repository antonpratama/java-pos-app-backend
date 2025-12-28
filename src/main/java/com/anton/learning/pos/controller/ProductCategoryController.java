package com.anton.learning.pos.controller;

import com.anton.learning.pos.dto.ProductCategoryRequest;
import com.anton.learning.pos.dto.ProductCategoryResponse;
import com.anton.learning.pos.dto.WebResponse;
import com.anton.learning.pos.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-category")
public class ProductCategoryController {

  @Autowired
  private ProductCategoryService service;

  @PostMapping("/")
  public ResponseEntity<WebResponse<ProductCategoryResponse>> create(@RequestBody ProductCategoryRequest request){

    return ResponseEntity.ok().body(WebResponse.<ProductCategoryResponse>builder()
                    .data(service.create(request))
                    .httpStatus(HttpStatus.CREATED)
            .build());
  }

  @GetMapping("/")
  public ResponseEntity<WebResponse<List<ProductCategoryResponse>>> getAll(){

    return ResponseEntity.ok().body(WebResponse.<List<ProductCategoryResponse>>builder()
            .data(service.getAllActive())
            .httpStatus(HttpStatus.OK)
            .build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<WebResponse<ProductCategoryResponse>> getById(@PathVariable String id){

    return ResponseEntity.ok().body(WebResponse.<ProductCategoryResponse>builder()
            .data(service.getById(id))
            .httpStatus(HttpStatus.OK)
            .build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<WebResponse<ProductCategoryResponse>> update(@PathVariable String id,
                                                                     @RequestBody ProductCategoryRequest request){

    return ResponseEntity.ok().body(WebResponse.<ProductCategoryResponse>builder()
            .data(service.update(id, request))
            .httpStatus(HttpStatus.OK)
            .build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<WebResponse<ProductCategoryResponse>> delete(@PathVariable String id){

    return ResponseEntity.ok().body(WebResponse.<ProductCategoryResponse>builder()
            .data(service.delete(id))
            .httpStatus(HttpStatus.OK)
            .build());
  }
}

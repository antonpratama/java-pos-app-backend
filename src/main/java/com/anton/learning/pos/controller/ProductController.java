package com.anton.learning.pos.controller;

import com.anton.learning.pos.dto.PagingResponse;
import com.anton.learning.pos.dto.ProductRequest;
import com.anton.learning.pos.dto.ProductResponse;
import com.anton.learning.pos.dto.WebResponse;
import com.anton.learning.pos.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService service;

  @PostMapping("/")
  public ResponseEntity<WebResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request){

    return ResponseEntity.ok().body(WebResponse.<ProductResponse>builder()
                    .data(service.createProduct(request))
                    .httpStatus(HttpStatus.CREATED)
            .build());
  }

  @GetMapping("/")
  public ResponseEntity<WebResponse<List<ProductResponse>>> getAllActiveProduct(){

    return ResponseEntity.ok().body(WebResponse.<List<ProductResponse>>builder()
            .data(service.getAllActiveProduct())
            .httpStatus(HttpStatus.OK)
            .build());
  }

  @GetMapping("/category/{id}")
  public ResponseEntity<WebResponse<List<ProductResponse>>> getAllProductByCategory(@PathVariable String id){

    return ResponseEntity.ok().body(WebResponse.<List<ProductResponse>>builder()
            .data(service.getAllProductByCategory(id))
            .httpStatus(HttpStatus.OK)
            .build());
  }

  @GetMapping("/search")
  public ResponseEntity<WebResponse<List<ProductResponse>>> searchProductsByName( @RequestParam(defaultValue = "") String query,
                                                                                  @RequestParam(defaultValue = "0") int page,
                                                                                  @RequestParam(defaultValue = "10") int size,
                                                                                  @RequestParam(defaultValue = "name") String sortBy,
                                                                                  @RequestParam(defaultValue = "asc") String direction){

    Page<ProductResponse> productResponses = service.searchProducts(query, page, size, sortBy, direction);

    return ResponseEntity.ok().body(WebResponse.<List<ProductResponse>>builder()
            .data(productResponses.getContent())
            .httpStatus(HttpStatus.OK)
                    .paging(PagingResponse.builder()
                            .totalElements(productResponses.getTotalElements())
                            .size(productResponses.getSize())
                            .currentPage(productResponses.getNumber())
                            .totalPage(productResponses.getTotalPages())
                            .build())
            .build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<WebResponse<ProductResponse>> getProductById(@PathVariable String id){

    return ResponseEntity.ok().body(WebResponse.<ProductResponse>builder()
            .data(service.getProductById(id))
            .httpStatus(HttpStatus.OK)
            .build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<WebResponse<ProductResponse>> updateProduct(@PathVariable String id,
                                                                    @Valid @RequestBody ProductRequest request){

    return ResponseEntity.ok().body(WebResponse.<ProductResponse>builder()
            .data(service.updateProduct(id, request))
            .httpStatus(HttpStatus.OK)
            .build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<WebResponse<ProductResponse>> deleteProduct(@PathVariable String id){

    return ResponseEntity.ok().body(WebResponse.<ProductResponse>builder()
            .data(service.deleteProduct(id))
            .httpStatus(HttpStatus.OK)
            .build());
  }
}

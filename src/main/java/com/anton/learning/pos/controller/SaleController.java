package com.anton.learning.pos.controller;

import com.anton.learning.pos.dto.PagingResponse;
import com.anton.learning.pos.dto.SaleRequest;
import com.anton.learning.pos.dto.SaleResponse;
import com.anton.learning.pos.dto.WebResponse;
import com.anton.learning.pos.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale")
public class SaleController {

  @Autowired
  private SaleService service;

  @PostMapping("/")
  public ResponseEntity<WebResponse<SaleResponse>> createSale(@RequestBody SaleRequest request){

    return ResponseEntity.ok().body(WebResponse.<SaleResponse>builder()
                    .data(service.createSale(request))
                    .httpStatus(HttpStatus.CREATED)
            .build());
  }

  @GetMapping("/")
  public ResponseEntity<WebResponse<List<SaleResponse>>> getAllActiveSales(){

    return ResponseEntity.ok().body(WebResponse.<List<SaleResponse>>builder()
            .data(service.getAllActiveSales())
            .httpStatus(HttpStatus.OK)
            .build());
  }

  @GetMapping("/cashier/{id}")
  public ResponseEntity<WebResponse<List<SaleResponse>>> getAllByCashier(@PathVariable String id,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size,
                                                                         @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                         @RequestParam(defaultValue = "asc") String direction){

    Page<SaleResponse> responses = service.getAllByCashierIdPageable(id, page, size, sortBy, direction);

    return ResponseEntity.ok().body(WebResponse.<List<SaleResponse>>builder()
            .data(responses.getContent())
            .paging(PagingResponse.builder()
                    .totalElements(responses.getTotalElements())
                    .size(responses.getSize())
                    .currentPage(responses.getNumber())
                    .totalPage(responses.getTotalPages())
                    .build())
            .httpStatus(HttpStatus.OK)
            .build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<WebResponse<SaleResponse>> getSaleById(@PathVariable String id){

    return ResponseEntity.ok().body(WebResponse.<SaleResponse>builder()
            .data(service.getSaleById(id))
            .httpStatus(HttpStatus.OK)
            .build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<WebResponse<SaleResponse>> softDeleteSaleById(@PathVariable String id){

    return ResponseEntity.ok().body(WebResponse.<SaleResponse>builder()
            .data(service.softDeleteSaleById(id))
            .httpStatus(HttpStatus.OK)
            .build());
  }
}

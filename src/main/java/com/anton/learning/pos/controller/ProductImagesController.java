package com.anton.learning.pos.controller;

import com.anton.learning.pos.dto.ProductImagesResponse;
import com.anton.learning.pos.dto.WebResponse;
import com.anton.learning.pos.service.ProductImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("product-images")
public class ProductImagesController {

  @Autowired
  private ProductImagesService productImagesService;

  @PutMapping("/")
  public ResponseEntity<WebResponse<List<ProductImagesResponse>>> updateImage(@RequestPart("productId") String productId,
                                                                              @RequestPart("images")MultipartFile[] imagesFile){
    return ResponseEntity.ok().body(WebResponse.<List<ProductImagesResponse>>builder()
            .httpStatus(HttpStatus.OK)
            .data(productImagesService.upsertImage(productId, imagesFile))
            .build());
  }

  @GetMapping("/{productId}")
  public ResponseEntity<WebResponse<List<ProductImagesResponse>>> getImagesByProductId(@PathVariable("productId") String productId){
    return ResponseEntity.ok().body(WebResponse.<List<ProductImagesResponse>>builder()
            .httpStatus(HttpStatus.OK)
            .data(productImagesService.getImageByProductId(productId))
            .build());
  }

  @DeleteMapping("/{imageId}")
  public ResponseEntity<WebResponse<Void>> deleteImagesByImageId(@PathVariable("imageId") String imageId){
    productImagesService.deleteImage(imageId);

    return ResponseEntity.ok().body(WebResponse.<Void>builder()
                    .httpStatus(HttpStatus.OK)
            .build());
  }


}

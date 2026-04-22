package com.anton.learning.pos.service;

import com.anton.learning.pos.dto.ProductImagesResponse;
import com.anton.learning.pos.entity.Product;
import com.anton.learning.pos.entity.ProductImages;
import com.anton.learning.pos.repository.ProductImagesRepository;
import com.anton.learning.pos.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class ProductImagesService {

  @Autowired
  private ProductImagesRepository imagesRepository;

  @Autowired
  private ProductRepository productRepository;

  private ProductImagesResponse toProductImagesResponse(ProductImages image){
    log.info("Fetching product image from DB: {}", image.getId());
    String base64 = Base64.getEncoder().encodeToString(image.getImageData());
    log.info("Base64 encoding done.");

    return ProductImagesResponse.builder()
            .id(image.getId())
            .mimeType(image.getMimeType())
            .imageIndex(image.getImageIndex())
            .base64Image("data:"+image.getMimeType()+";base64,"+base64)
            .build();
  }

  @Transactional
  public List<ProductImagesResponse> uploadImage(String productId,
                                                  MultipartFile[] multipartFile) {
    if (multipartFile.length == 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No image file uploaded");
    }

    long existingImages = imagesRepository.countByProduct_IdAndIsActiveTrue(productId);
    if (existingImages + multipartFile.length > 3) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum 3 images per product allowed");
    }

    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Id Not Found"));

    List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/webp");
    for (MultipartFile file : multipartFile) {
      if (!allowedTypes.contains(file.getContentType())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image MIME type");
      }
    }

    int startIndex = (int)existingImages;
    List<ProductImagesResponse> listImageResponse = new ArrayList<>();
    try {
      for (int i = 0; i < multipartFile.length; i++) {
        ProductImages image = new ProductImages();
        image.setProduct        (product);
        image.setIsActive       (true);
        image.setMimeType       (multipartFile[i].getContentType());
        image.setImageIndex     (i + startIndex + 1);
        image.setImageData      (multipartFile[i].getBytes());

        image = imagesRepository.save(image);
        listImageResponse.add(toProductImagesResponse(image));
      }
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to read image data", e);
    }

    return listImageResponse;
  }

  @Transactional
  public List<ProductImagesResponse> upsertImage(String productId,
                                                 MultipartFile[] multipartFile) {
    if (multipartFile.length == 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No image file uploaded");
    }

    if (multipartFile.length > 3) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum 3 images per product allowed");
    }

    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Id Not Found"));

    List<ProductImages> activeImagesList = imagesRepository.findByProduct_idAndIsActiveTrue(productId);
    activeImagesList.forEach(productImages -> productImages.setIsActive(false));
    imagesRepository.saveAll(activeImagesList);

    List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/webp");
    for (MultipartFile file : multipartFile) {
      if (!allowedTypes.contains(file.getContentType())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image MIME type: " + file.getContentType());
      }
    }

    List<ProductImagesResponse> listImageResponse = new ArrayList<>();
    try {
      for (int i = 0; i < multipartFile.length; i++) {
        ProductImages image = new ProductImages();
        image.setProduct        (product);
        image.setIsActive       (true);
        image.setMimeType       (multipartFile[i].getContentType());
        image.setImageIndex     (i + 1);
        image.setImageData      (multipartFile[i].getBytes());

        image = imagesRepository.save(image);
        listImageResponse.add(toProductImagesResponse(image));
      }
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to read image data", e);
    }

    return listImageResponse;
  }

  @Transactional(readOnly = true)
  public List<ProductImagesResponse> getImageByProductId(String productId){
    productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Id Not Found"));

    return imagesRepository.findByProduct_idAndIsActiveTrue(productId).stream()
            .map(this::toProductImagesResponse)
            .toList();
  }

  @Transactional(readOnly = true)
  public List<ProductImagesResponse> getMainImageByProductId(String productId){
    productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Id Not Found"));

    return imagesRepository.findByProduct_idAndIsActiveTrueAndImageIndex(productId,1).stream()
            .map(this::toProductImagesResponse)
            .toList();
  }

  public void deleteImage(String imageId){
    ProductImages image = imagesRepository.findById(imageId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));

    image.setIsActive(false);

    imagesRepository.save(image);
  }
}

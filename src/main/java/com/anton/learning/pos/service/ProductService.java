package com.anton.learning.pos.service;

import com.anton.learning.pos.dto.ProductCategoryResponse;
import com.anton.learning.pos.dto.ProductImagesResponse;
import com.anton.learning.pos.dto.ProductRequest;
import com.anton.learning.pos.dto.ProductResponse;
import com.anton.learning.pos.entity.Product;
import com.anton.learning.pos.entity.ProductCategory;
import com.anton.learning.pos.entity.ProductImages;
import com.anton.learning.pos.repository.ProductCategoryRepository;
import com.anton.learning.pos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductCategoryRepository productCategoryRepository;

  @Autowired
  private ProductImagesService productImagesService;

  private ProductCategoryResponse toProductCategoryResponse(ProductCategory category){

    return ProductCategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .description(category.getDescription())
            .isActive(category.getIsActive())
            .build();
  }

  private ProductResponse toProductResponse(Product product){
    return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .sku(product.getSku())
            .isActive(product.getIsActive())
            .description(product.getDescription())
            .price(product.getPrice())
            .stock(product.getStock())
            .category(toProductCategoryResponse(product.getCategory()))
            .build();
  }

  private ProductResponse toSearchResponse(Product product){
    return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .sku(product.getSku())
            .isActive(product.getIsActive())
            .description(product.getDescription())
            .price(product.getPrice())
            .stock(product.getStock())
            .category(toProductCategoryResponse(product.getCategory()))
            .images(productImagesService.getMainImageByProductId(product.getId()))
            .build();
  }

  private ProductImagesResponse toProductImagesResponse(ProductImages image){
    String base64 = Base64.getEncoder().encodeToString(image.getImageData());

    return ProductImagesResponse.builder()
            .id(image.getId())
            .mimeType(image.getMimeType())
            .imageIndex(image.getImageIndex())
            .base64Image("data:"+image.getMimeType()+";base64,"+base64)
            .build();
  }

  public ProductResponse createProduct(ProductRequest request){
    productRepository.findBySku(request.getSku())
            .ifPresent(p -> {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SKU already exists");});

    ProductCategory category = productCategoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));

    Product product = new Product();

    product.setName         (request.getName());
    product.setSku          (request.getSku());
    product.setDescription  (request.getDescription());
    product.setPrice        (request.getPrice());
    product.setStock        (request.getStock());
    product.setIsActive     (true);
    product.setCategory     (category);

    product = productRepository.save(product);

    return toProductResponse(product);
  }

  public Page<ProductResponse> searchProducts(String name, int page, int size, String sortBy, String direction){
    Sort sort = direction.equalsIgnoreCase("desc") ?
            Sort.by(sortBy).descending() :
            Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Product> listResponse = productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name, pageable);

    return listResponse.map(this::toSearchResponse);
  }

  public List<ProductResponse> getAllActiveProduct(){

    return productRepository.findAllByIsActiveTrue().stream()
            .map(this::toProductResponse)
            .collect(Collectors.toList());
  }

  public List<ProductResponse> getAllProductByCategory(String categoryId){

    return productRepository.findByCategory_IdAndIsActiveTrue(categoryId).stream()
            .map(this::toProductResponse)
            .collect(Collectors.toList());
  }

  public ProductResponse getProductById(String id){
    Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

    return toProductResponse(product);
  }

  public ProductResponse updateProduct(String id, ProductRequest request){
    Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

    productRepository.findBySku(request.getSku())
            .filter(product -> !product.getId().equals(id))
            .ifPresent(product -> {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SKU already exists");});

    ProductCategory category = productCategoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));

    existingProduct.setName         (request.getName());
    existingProduct.setDescription  (request.getDescription());
    existingProduct.setSku          (request.getSku());
    existingProduct.setPrice        (request.getPrice());
    existingProduct.setStock        (request.getStock());
    existingProduct.setCategory     (category);

    Product updated = productRepository.save(existingProduct);

    return toProductResponse(updated);
  }

  public ProductResponse deleteProduct(String id){
    Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

    if (!product.getIsActive()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status already not active");
    }

    product.setIsActive(false);

    Product deletedProduct = productRepository.save(product);

    return toProductResponse(deletedProduct);
  }
}

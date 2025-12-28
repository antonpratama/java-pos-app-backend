package com.anton.learning.pos.service;

import com.anton.learning.pos.dto.ProductCategoryRequest;
import com.anton.learning.pos.dto.ProductCategoryResponse;
import com.anton.learning.pos.entity.ProductCategory;
import com.anton.learning.pos.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryService {

  @Autowired
  private ProductCategoryRepository repository;

  private ProductCategoryResponse toProductCategoryResponse(ProductCategory category){

    return ProductCategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .description(category.getDescription())
            .isActive(category.getIsActive())
            .build();
  }

  public ProductCategoryResponse create(ProductCategoryRequest request){
    if (repository.existsByName(request.getName())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name already exists");
    }

    ProductCategory category = new ProductCategory();
    category.setName          (request.getName());
    category.setDescription   (request.getDescription());
    category.setIsActive      (true);

    category = repository.save(category);

    return toProductCategoryResponse(category);
  }

  public ProductCategoryResponse getById(String id){
    ProductCategory category = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Category Not Found"));

    return toProductCategoryResponse(category);
  }

  public List<ProductCategoryResponse> getAllActive(){

    return repository.findAllByIsActiveTrue().stream()
            .map(this::toProductCategoryResponse)
            .collect(Collectors.toList());
  }

  public ProductCategoryResponse update(String id, ProductCategoryRequest request){
    ProductCategory category = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Category Not Found"));

    if (!category.getName().equals(request.getName()) &&
            repository.existsByName(request.getName())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name already exists");
    }

    category.setName          (request.getName());
    category.setDescription   (request.getDescription());
    ProductCategory updated = repository.save(category);

    return toProductCategoryResponse(updated);
  }

  public ProductCategoryResponse delete(String id){
    ProductCategory category = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Category Not Found"));


    category.setIsActive(false);
    ProductCategory deleted = repository.save(category);

    return toProductCategoryResponse(deleted);
  }
}

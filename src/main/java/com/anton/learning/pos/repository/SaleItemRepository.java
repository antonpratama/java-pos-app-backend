package com.anton.learning.pos.repository;

import com.anton.learning.pos.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, String> {
  List<SaleItem> findBySale_Id(String saleId);
}

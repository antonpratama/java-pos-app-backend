package com.anton.learning.pos.repository;

import com.anton.learning.pos.entity.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryLogRepository extends JpaRepository<InventoryLog, String> {

  List<InventoryLog> findByProduct_Id(String productId);
}

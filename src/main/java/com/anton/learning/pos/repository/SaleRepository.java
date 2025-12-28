package com.anton.learning.pos.repository;

import com.anton.learning.pos.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, String> {

  Optional<Sale> findTopByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startOfDay, LocalDateTime endOfDay);


  List<Sale> findByCashier_Id(String cashierId);
  List<Sale> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
  List<Sale> findByIsActiveTrue();
  Optional<Sale> findByIdAndIsActiveTrue(String id);
  List<Sale> findByCashier_IdAndIsActiveTrue(String cashierId);
  Page<Sale> findByCashier_IdAndIsActiveTrue(String cashierId, Pageable pageable);
}

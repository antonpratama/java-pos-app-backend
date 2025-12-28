package com.anton.learning.pos.service;

import com.anton.learning.pos.dto.SaleItemRequest;
import com.anton.learning.pos.dto.SaleItemResponse;
import com.anton.learning.pos.dto.SaleRequest;
import com.anton.learning.pos.dto.SaleResponse;
import com.anton.learning.pos.entity.Product;
import com.anton.learning.pos.entity.Sale;
import com.anton.learning.pos.entity.SaleItem;
import com.anton.learning.pos.entity.User;
import com.anton.learning.pos.repository.ProductRepository;
import com.anton.learning.pos.repository.SaleItemRepository;
import com.anton.learning.pos.repository.SaleRepository;
import com.anton.learning.pos.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SaleRepository saleRepository;

  @Autowired
  private SaleItemRepository saleItemRepository;

  @Autowired
  private ProductRepository productRepository;

  public static String padNumber(int number, int length) {
    return String.format("%0" + length + "d", number);
  }


  private SaleItemResponse toSaleItemResponse(SaleItem saleItem){
    return SaleItemResponse.builder()
            .id(saleItem.getId())
            .productName(saleItem.getProduct().getName())
            .quantity(saleItem.getQuantity())
            .price(saleItem.getPrice())
            .subtotal(saleItem.getSubtotal())
            .productId(saleItem.getProduct().getId())
            .build();
  }

  private SaleResponse toSaleResponse(Sale sale){

    List<SaleItemResponse> saleItemResponseList = sale.getItems().stream()
            .map(this::toSaleItemResponse)
            .toList();


    return SaleResponse.builder()
            .id(sale.getId())
            .cashierId(sale.getCashier().getId())
            .cashierName(sale.getCashier().getUsername())
            .totalAmount(sale.getTotalAmount())
            .paidAmount(sale.getPaidAmount())
            .changeAmount(sale.getChangeAmount())
            .invoiceNumber(sale.getInvoiceNumber())
            .paymentType(sale.getPaymentType())
            .saleItems(saleItemResponseList)
            .createdAt(sale.getCreatedAt())
            .build();
  }

  private String generateInvoiceNumber(){
    //example SALE-20250731-0001
    String invoiceNumber = "";
    int desiredLength = 4;
    LocalDate now = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

//    Optional<Sale> optLastSale = saleRepository.findByCreatedAtLimitOne();
    Optional<Sale> optLastSale = saleRepository.findTopByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime.now(), LocalDateTime.now());
    if (optLastSale.isPresent()){
      Sale        lastSale     = optLastSale.get();
      invoiceNumber            = lastSale.getInvoiceNumber();

      String[]    split        = invoiceNumber.split("-");
      int         noUrutBaru   = Integer.parseInt(split[split.length-1]) + 1;
      String      paddedNumber = padNumber(noUrutBaru, desiredLength);
      invoiceNumber            = "SALE-"+formatter.format(now)+"-"+paddedNumber;
    } else {
      invoiceNumber            = "SALE-"+formatter.format(now)+"-0001";
    }

    return invoiceNumber;
  }

  @Transactional
  public SaleResponse createSale(SaleRequest request) {
    List<SaleItem> saleItemList = new ArrayList<>();
    User cashier = userRepository.findById(request.getCashierId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    Sale sale = new Sale();
    sale.setCashier         (cashier);
    sale.setPaymentType     (request.getPaymentType());
    sale.setPaidAmount      (request.getPaidAmount());
    sale.setIsActive        (true);
    sale.setInvoiceNumber   (generateInvoiceNumber());

    for (SaleItemRequest itemRequest : request.getSaleItems()) {
      Product product = productRepository.findById(itemRequest.getProductId())
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

      SaleItem saleItem = new SaleItem();
      saleItem.setSale          (sale);
      saleItem.setProduct       (product);
      saleItem.setPrice         (product.getPrice());
      saleItem.setQuantity      (itemRequest.getQuantity());
      saleItem.setSubtotal      (product.getPrice().multiply(new BigDecimal(itemRequest.getQuantity())));
      saleItemList.add(saleItem);
    }

    BigDecimal totalAmount = saleItemList.stream()
            .map(SaleItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);


    if (request.getPaidAmount().compareTo(totalAmount) < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paid amount is less than total amount.");
    }

    sale.setItems(saleItemList);
    sale.setTotalAmount(totalAmount);
    sale.setChangeAmount(request.getPaidAmount().subtract(totalAmount));
    sale = saleRepository.save(sale);

    return toSaleResponse(sale);
  }

  public List<SaleResponse> getAllActiveSales(){

    return saleRepository.findByIsActiveTrue().stream()
            .map(this::toSaleResponse)
            .toList();
  }

  public List<SaleResponse> getAllByCashierId(String cashierId){

    return saleRepository.findByCashier_IdAndIsActiveTrue(cashierId).stream()
            .map(this::toSaleResponse)
            .toList();
  }

  public Page<SaleResponse> getAllByCashierIdPageable(String cashierId, int page, int size, String sortBy, String direction){

    Sort sort = direction.equalsIgnoreCase("desc") ?
            Sort.by(sortBy).descending() :
            Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Sale> sales = saleRepository.findByCashier_IdAndIsActiveTrue(cashierId, pageable);

    return sales.map(this::toSaleResponse);
  }

  public SaleResponse getSaleById(String id){
    Sale sale = saleRepository.findByIdAndIsActiveTrue(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sales not found"));

    return toSaleResponse(sale);
  }

  public SaleResponse softDeleteSaleById(String id){
    Sale sale = saleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sales not found"));

    if (!sale.getIsActive()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sale already deleted");
    }

    sale.setIsActive(false);
    sale = saleRepository.save(sale);
    return toSaleResponse(sale);
  }
}

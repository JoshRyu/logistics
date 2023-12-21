package com.madeg.logistics.repository;

import com.madeg.logistics.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {
  Page<Product> findAll(Pageable pageable);

  Product findByName(String name);

  Product findByProductCode(String productCode);

  @Query("SELECT MAX(p.productCode) FROM Product p")
  String findLastProductCode();
}

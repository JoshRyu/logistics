package com.madeg.logistics.repository;

import com.madeg.logistics.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
  Page<Product> findAll(Specification<Product> spec, Pageable pageable);

  Product findByName(String name);

  Product findByProductCode(String productCode);

  @Query("SELECT MAX(p.productCode) FROM Product p")
  String findLastProductCode();
}
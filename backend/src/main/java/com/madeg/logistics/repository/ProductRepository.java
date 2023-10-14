package com.madeg.logistics.repository;

import com.madeg.logistics.entity.Product;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {

    Product findByName(String name);

    @Query("SELECT MAX(p.productCode) FROM Product p")
    String findLastProductCode();
}
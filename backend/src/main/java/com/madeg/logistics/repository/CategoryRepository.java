package com.madeg.logistics.repository;

import com.madeg.logistics.entity.Category;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, String> {
  Category findByName(String name);

  Category findByCategoryCode(String categoryCode);

  List<Category> findAll();
}

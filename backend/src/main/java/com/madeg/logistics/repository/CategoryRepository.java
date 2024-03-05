package com.madeg.logistics.repository;

import com.madeg.logistics.entity.Category;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
  Category findByName(String name);

  Category findByCategoryCode(String categoryCode);

  List<Category> findByParentCategoryCategoryCode(String CategoryCode);

  List<Category> findAll();
}

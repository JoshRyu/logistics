package com.madeg.logistics.repository;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.madeg.logistics.entity.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, String> {

    Category findByName(String name);

    Category findByCategoryCode(String code);

    List<Category> findAll();
}

package com.project.ecommerce.repository;

import com.project.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByNameContainingIgnoreCaseAndDeletedFalse(String name);

    List<Product> findByNameContainingIgnoreCaseAndCategoryAndDeletedFalse(String name, String category);

    List<Product> findByCategoryAndDeletedFalseAndIdNotIn(String category, List<String> ids);

    boolean existsByName(String name);
}
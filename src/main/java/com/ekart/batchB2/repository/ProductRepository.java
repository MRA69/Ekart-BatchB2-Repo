package com.ekart.batchB2.repository;

import com.ekart.batchB2.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsByName(String name);
    Optional<Product> findByCategoryId(String categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
}

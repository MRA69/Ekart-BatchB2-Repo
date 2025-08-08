package com.ekart.batchB2.repository;

import com.ekart.batchB2.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    // Additional query methods can be defined here if needed
}

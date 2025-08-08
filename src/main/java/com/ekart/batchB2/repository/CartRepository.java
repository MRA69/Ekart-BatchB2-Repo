package com.ekart.batchB2.repository;

import com.ekart.batchB2.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {
    // Additional query methods can be defined here if needed
}

package com.ekart.batchB2.repository;

import com.ekart.batchB2.entity.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends MongoRepository<CartItem, String> {
    void deleteByProductName(String productName);
}

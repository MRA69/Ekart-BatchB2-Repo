package com.ekart.batchB2.repository;

import com.ekart.batchB2.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
    // Additional query methods can be defined here if needed
}

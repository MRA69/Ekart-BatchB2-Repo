package com.ekart.batchB2.repository;

import com.ekart.batchB2.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    Order findByUserId(String userId);
}

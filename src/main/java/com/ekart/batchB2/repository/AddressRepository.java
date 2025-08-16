package com.ekart.batchB2.repository;

import com.ekart.batchB2.entity.Address;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends MongoRepository<Address, String> {
    // Additional query methods can be defined here if needed
}

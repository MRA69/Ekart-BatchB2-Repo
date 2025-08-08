package com.ekart.batchB2.repository;

import com.ekart.batchB2.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);
}

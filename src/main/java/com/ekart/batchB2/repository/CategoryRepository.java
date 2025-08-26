package com.ekart.batchB2.repository;

import com.ekart.batchB2.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    boolean existsByName(String name);
    Category findByName(String name);
    void deleteByName(String name);
}

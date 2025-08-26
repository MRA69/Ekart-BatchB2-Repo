package com.ekart.batchB2.controller;

import com.ekart.batchB2.dto.CategoryDTO;
import com.ekart.batchB2.exceptionhandler.CategoryNotFoundException;
import com.ekart.batchB2.exceptionhandler.DuplicateCategoryException;
import com.ekart.batchB2.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
@Validated
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "/getAll", produces = "application/json")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping(value = "/getByName/{name}", produces = "application/json")
    public ResponseEntity<CategoryDTO> getCategoryByName(@PathVariable ("name") String name) throws CategoryNotFoundException {
        CategoryDTO category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(category);
    }

    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) throws DuplicateCategoryException {
        String response = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/createBatch", consumes = "application/json")
    public ResponseEntity<List<String>> createCategories(@RequestBody @Valid List<CategoryDTO> categoryDTOs) throws DuplicateCategoryException {
        List<String> response = categoryService.createCategories(categoryDTOs);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<String> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO) throws CategoryNotFoundException {
        String response = categoryService.updateCategory(categoryDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/delete/{name}")
    public ResponseEntity<String> deleteCategory(@PathVariable("name") String name) throws CategoryNotFoundException {
        String response = categoryService.deleteCategory(name);
        return ResponseEntity.ok(response);
    }
}

package com.ekart.batchB2.service;

import com.ekart.batchB2.dto.CategoryDTO;
import com.ekart.batchB2.exceptionhandler.CategoryNotFoundException;
import com.ekart.batchB2.exceptionhandler.DuplicateCategoryException;

import java.util.List;

public interface CategoryService {

    String createCategory(CategoryDTO categoryDTO) throws DuplicateCategoryException;
    List<String> createCategories(List<CategoryDTO> categoryDTOs) throws DuplicateCategoryException;
    String updateCategory(CategoryDTO categoryDTO) throws CategoryNotFoundException;
    String deleteCategory(String name) throws CategoryNotFoundException;
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryByName(String name) throws CategoryNotFoundException;
}

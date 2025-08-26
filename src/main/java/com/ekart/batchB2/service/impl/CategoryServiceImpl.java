package com.ekart.batchB2.service.impl;

import com.ekart.batchB2.dto.CategoryDTO;
import com.ekart.batchB2.dto.ProductDTO;
import com.ekart.batchB2.entity.Category;
import com.ekart.batchB2.entity.Product;
import com.ekart.batchB2.exceptionhandler.CategoryNotFoundException;
import com.ekart.batchB2.exceptionhandler.DuplicateCategoryException;
import com.ekart.batchB2.mapper.CategoryMapper;
import com.ekart.batchB2.mapper.ProductMapper;
import com.ekart.batchB2.repository.CategoryRepository;
import com.ekart.batchB2.repository.ProductRepository;
import com.ekart.batchB2.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductRepository productRepository;

    @Override
    public String createCategory(CategoryDTO categoryDTO) throws DuplicateCategoryException{
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            logger.error("Category with name {} already exists", categoryDTO.getName());
            throw new DuplicateCategoryException("Category with name " + categoryDTO.getName() + " already exists.");
        }
        categoryRepository.save(categoryMapper.prepareCategoryEntity(categoryDTO));
        logger.info("Category {} created successfully", categoryDTO.getName());
        return "Category "+categoryDTO.getName()+" created successfully";
    }

    @Override
    public List<String> createCategories(List<CategoryDTO> categoryDTOs) throws DuplicateCategoryException {
        List<String> createdCategories = new ArrayList<>();
        List<String> duplicateCategories = new ArrayList<>();
        for(CategoryDTO categoryDTO : categoryDTOs){
            if(categoryRepository.existsByName(categoryDTO.getName())) {
                duplicateCategories.add(categoryDTO.getName());
            } else {
                logger.info("Creating category {}", categoryDTO.getName());
                categoryRepository.save(categoryMapper.prepareCategoryEntity(categoryDTO));
                createdCategories.add("Category "+categoryDTO.getName()+" created successfully");
            }
        }
        if (!duplicateCategories.isEmpty()) {
            logger.error("Categories with names {} already exist", String.join(", ", duplicateCategories));
            throw new DuplicateCategoryException("Category with name " + String.join(", ", duplicateCategories) + " already exist.");
        }
        return createdCategories;
    }

    @Override
    public String updateCategory(CategoryDTO categoryDTO) throws CategoryNotFoundException {
        Category existingCategory = categoryRepository.findByName(categoryDTO.getName());
        if(existingCategory == null) throw new CategoryNotFoundException("Category details not found.");

        else{
            if (!categoryRepository.existsByName(categoryDTO.getName())) {
                logger.error("Category with name {} not found", categoryDTO.getName());
                throw new CategoryNotFoundException("Category with name " + categoryDTO.getName() + " not found.");
            }
            if(categoryDTO.getName() != null) existingCategory.setName(categoryDTO.getName());
            if(categoryDTO.getDescription() != null) existingCategory.setDescription(categoryDTO.getDescription());
            categoryRepository.save(existingCategory);
            List<Product> products = productRepository.findByCategoryId(existingCategory.getName());
            for(Product product : products){
                ProductDTO productDTO = new ProductDTO();
                productDTO.setCategoryId(categoryDTO.getName());
                product = productMapper.prepareProductEntity(productDTO);
                productRepository.save(product);
            }
            logger.info("Updating Products with category {}", categoryDTO.getName());
            logger.info("Category {} updated successfully", categoryDTO.getName());
            return "Category " + categoryDTO.getName() + " updated successfully";
        }
    }

    @Override
    public String deleteCategory(String name) throws CategoryNotFoundException {
        if (!categoryRepository.existsByName(name)) {
            logger.error("Category with name {} not found", name);
            throw new CategoryNotFoundException("Category with name " + name + " not found.");
        }
        logger.info("Deleting category with name: {}", name);
        // Delete all products associated with this category
        productRepository.deleteByCategoryId(name);
        logger.info("Products associated with category {} deleted", name);
        categoryRepository.deleteByName(name);
        logger.info("Category {} deleted successfully", name);
        return "Category " + name + " deleted successfully";
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        logger.info("Fetching all categories");
        return categoryRepository.findAll().stream()
                .map(categoryMapper::prepareCategoryDTO)
                .toList();
    }

    @Override
    public CategoryDTO getCategoryByName(String name) throws CategoryNotFoundException {
        logger.info("Fetching category with name: {}", name);
        if(!categoryRepository.existsByName(name)) {
            logger.error("Category with name {} not found", name);
            throw new CategoryNotFoundException("Category with name " + name + " not found.");
        }
        return categoryMapper.prepareCategoryDTO(categoryRepository.findByName(name));
    }
}

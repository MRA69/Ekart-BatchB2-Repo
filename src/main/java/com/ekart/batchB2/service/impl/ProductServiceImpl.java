package com.ekart.batchB2.service.impl;

import com.ekart.batchB2.dto.ProductDTO;
import com.ekart.batchB2.dto.ProductNVDTO;
import com.ekart.batchB2.entity.Product;
import com.ekart.batchB2.exceptionhandler.DuplicateProductException;
import com.ekart.batchB2.exceptionhandler.ProductNotFoundExcption;
import com.ekart.batchB2.mapper.ProductMapper;
import com.ekart.batchB2.repository.ProductRepository;
import com.ekart.batchB2.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    @Override
    public String createProduct(ProductDTO productDTO) throws DuplicateProductException {
        logger.info("Creating a new product with name: {}", productDTO.getName());
        if(productRepository.existsByName(productDTO.getName())) {
            logger.warn("Product with name {} already exists", productDTO.getName());
            throw new DuplicateProductException("Product with name " + productDTO.getName() + " already exists");
        }
        productRepository.save(productMapper.prepareProductEntity(productDTO));
        logger.info("Product {} created successfully", productDTO.getName()); // Log the successful creation of the product
        return "Product "+productDTO.getName()+" added successfully";
    }

    @Override
    public List<String> createProductBulk(List<ProductDTO> productDTO) throws DuplicateProductException {
        List<String> prductNames = new ArrayList<>();
        List<String> duplicateNames = new ArrayList<>();
        logger.info("Creating bulk products");
        for (ProductDTO dto : productDTO) {
            if (productRepository.existsByName(dto.getName())) {
                logger.warn("Product with name {} already exists", dto.getName());
                duplicateNames.add("Product with name " + dto.getName() + " already exists");
            }
            else {
                logger.info("Product {} created successfully", dto.getName());
                productRepository.save(productMapper.prepareProductEntity(dto));
                prductNames.add("Product "+dto.getName()+" added successfully");
            }
        }
        if (!duplicateNames.isEmpty()) {
            logger.warn("Duplicate products found: {}", duplicateNames);
            throw new DuplicateProductException("Duplicate products found: " + String.join(", ", duplicateNames));
        }
        return prductNames;
    }

    @Override
    public ProductDTO getProductById(String id) throws ProductNotFoundExcption {
        logger.info("Fetching product with ID: {}", id);
        return productRepository.findById(id)
                .map(productMapper::prepareProductDTO)
                .orElseThrow(() -> new ProductNotFoundExcption("Product with ID " + id + " not found"));
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll().stream()
                .map(productMapper::prepareProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByName(String name) {
        logger.info("Fetching products with name containing: {}", name);
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(productMapper::prepareProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String categoryId) throws ProductNotFoundExcption{
        logger.info("Fetching products for category ID: {}", categoryId);
        List<Product> product = productRepository.findByCategoryId(categoryId);
        if (product == null || product.isEmpty()) {
            logger.warn("No products found for category ID: {}", categoryId);
            throw new ProductNotFoundExcption("No products found for category ID " + categoryId);
        }
        return product.stream().map(productMapper::prepareProductDTO).collect(Collectors.toList());
    }

    @Override
    public String updateProduct(ProductNVDTO productDTO) throws ProductNotFoundExcption {
        logger.info("Updating product with name: {}", productDTO.getName());
        Product existingProduct = productRepository.findByName(productDTO.getName());
        if(productDTO == null || productDTO.getName() == null) throw new ProductNotFoundExcption("No fields to update provided");

        else {
            if (!productRepository.existsByName(productDTO.getName())) {
                logger.warn("Product with name {} not found", productDTO.getName());
                throw new ProductNotFoundExcption("Product with name " + productDTO.getName() + " not found");
            }
            if (productDTO.getName() != null) existingProduct.setName(productDTO.getName());
            if (productDTO.getDescription() != null) existingProduct.setDescription(productDTO.getDescription());
            if (productDTO.getPrice() != null) existingProduct.setPrice(productDTO.getPrice());
            if (productDTO.getStockQuantity() != null) existingProduct.setStockQuantity(productDTO.getStockQuantity());
            if (productDTO.getCategoryId() != null) existingProduct.setCategoryId(productDTO.getCategoryId());
            productRepository.save(existingProduct);

            logger.info("Product {} updated successfully", productDTO.getName());
            return "Product " + productDTO.getName() + " updated successfully";
        }
    }

    @Override
    public String deleteProduct(String name) throws ProductNotFoundExcption {
        logger.info("Deleting product with name: {}", name);
        if (!productRepository.existsByName(name)) {
            logger.warn("Product with name {} not found", name);
            throw new ProductNotFoundExcption("Product with name " + name + " not found");
        }
        productRepository.deleteByName(name);
        logger.info("Product with name {} deleted successfully", name);
        return "Product "+name+" deleted successfully";
    }
}

package com.ekart.batchB2.service.impl;

import com.ekart.batchB2.dto.ProductDTO;
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
import java.util.Optional;
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
        logger.info("Product {} created successfully", productDTO.getName());
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
            else
                productRepository.save(productMapper.prepareProductEntity(dto));
        }
        if (!duplicateNames.isEmpty()) {
            logger.warn("Duplicate products found: {}", duplicateNames);
            throw new DuplicateProductException("Duplicate products found: " + String.join(", ", duplicateNames));
        }
        for (ProductDTO dto : productDTO) {
            logger.info("Product {} created successfully", dto.getName());
            prductNames.add("Product "+dto.getName()+" added successfully");
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
        Optional<Product> productOptional = productRepository.findByCategoryId(categoryId);
        if (productOptional.isEmpty()) {
            logger.warn("No products found for category ID: {}", categoryId);
            throw new ProductNotFoundExcption("No products found for category ID " + categoryId);
        }
        return productRepository.findAll().stream()
                .filter(product -> product.getCategoryId().equals(categoryId))
                .map(productMapper::prepareProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String updateProduct(String id, ProductDTO productDTO) throws ProductNotFoundExcption {
        logger.info("Updating product with ID: {}", id);
        if (!productRepository.existsById(id)) {
            logger.warn("Product with ID {} not found", id);
            throw new ProductNotFoundExcption("Product with ID " + id + " not found");
        }
        if(!productRepository.existsByName(productDTO.getName())){
            logger.warn("Product with name {} does not exist", productDTO.getName());
            throw new ProductNotFoundExcption("Product with name " + productDTO.getName() + " does not exist");
        }
        Product product = productMapper.prepareProductEntity(productDTO);
        productRepository.save(product);
        logger.info("Product with ID {} updated successfully", id);
        return "Product "+id+" updated successfully";
    }

    @Override
    public String deleteProduct(String id) throws ProductNotFoundExcption {
        logger.info("Deleting product with ID: {}", id);
        if (!productRepository.existsById(id)) {
            logger.warn("Product with ID {} not found", id);
            throw new ProductNotFoundExcption("Product with ID " + id + " not found");
        }
        productRepository.deleteById(id);
        logger.info("Product with ID {} deleted successfully", id);
        return "Product "+id+" deleted successfully";
    }
}

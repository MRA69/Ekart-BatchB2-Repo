package com.ekart.batchB2.service;

import com.ekart.batchB2.dto.ProductDTO;
import com.ekart.batchB2.dto.ProductNVDTO;
import com.ekart.batchB2.exceptionhandler.DuplicateProductException;
import com.ekart.batchB2.exceptionhandler.ProductNotFoundExcption;

import java.util.List;

public interface ProductService {
    String createProduct(ProductDTO productDTO) throws DuplicateProductException;
    List<String> createProductBulk(List<ProductDTO> productDTO) throws DuplicateProductException;
    ProductDTO getProductById(String id) throws ProductNotFoundExcption;
    List<ProductDTO> getAllProducts();
    List<ProductDTO> getProductsByCategory(String categoryId) throws ProductNotFoundExcption;
    List<ProductDTO> getProductsByName(String name);
    String updateProduct(ProductNVDTO productDTO) throws ProductNotFoundExcption;
    String deleteProduct(String id) throws ProductNotFoundExcption;
}

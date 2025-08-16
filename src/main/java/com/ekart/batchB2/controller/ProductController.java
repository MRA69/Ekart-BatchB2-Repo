package com.ekart.batchB2.controller;

import com.ekart.batchB2.dto.ProductDTO;
import com.ekart.batchB2.exceptionhandler.DuplicateProductException;
import com.ekart.batchB2.exceptionhandler.ProductNotFoundExcption;
import com.ekart.batchB2.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@Validated
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<String> createProduct(@RequestBody @Valid ProductDTO productDTO) throws DuplicateProductException {
        String response = productService.createProduct(productDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/createBulk", consumes = "application/json")
    public ResponseEntity<List<String>> createProductBulk(@RequestBody @Valid List<ProductDTO> productDTO) throws DuplicateProductException {
        List<String> response = productService.createProductBulk(productDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable ("id") String id) throws ProductNotFoundExcption {
        ProductDTO productDTO = productService.getProductById(id);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productList = productService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    @GetMapping(value = "/getByName", produces = "application/json")
    public ResponseEntity<List<ProductDTO>> getProductsByName(@RequestParam("q") String name) {
        List<ProductDTO> productList = productService.getProductsByName(name);
        return ResponseEntity.ok(productList);
    }

    @GetMapping(value = "/getByCategory/{categoryId}", produces = "application/json")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable("categoryId") String categoryId) throws ProductNotFoundExcption {
        List<ProductDTO> productList = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(productList);
    }

    @PutMapping(value = "/update/{id}", consumes = "application/json")
    public ResponseEntity<String> updateProduct(@PathVariable("id") String id, @RequestBody @Valid ProductDTO productDTO) throws ProductNotFoundExcption {
        String response = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") String id) throws ProductNotFoundExcption {
        String response = productService.deleteProduct(id);
        return ResponseEntity.ok(response);
    }
}

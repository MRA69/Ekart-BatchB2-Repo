package com.ekart.batchB2.mapper;

import com.ekart.batchB2.dto.ProductDTO;
import com.ekart.batchB2.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO prepareProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategoryId(product.getCategoryId());
        productDTO.setStockQuantity(product.getStockQuantity());
        return productDTO;
    }
    public Product prepareProductEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategoryId(productDTO.getCategoryId());
        product.setStockQuantity(productDTO.getStockQuantity());
        return product;
    }
}

package com.ekart.batchB2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @NotBlank(message = "Product name is required")
    private String name;
    @NotBlank(message = "Product description is required")
    private String description;
    @NotNull(message = "Product price is required")
    private Double price;
    @NotNull(message = "Stock quantity cannot be null")
    private Integer stockQuantity;
    @NotBlank(message = "Category ID is required")
    private String categoryId;
}

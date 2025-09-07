package com.ekart.batchB2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    @Id
    private String id;
    private String productName;
    private Double price;
    private Integer quantity;
    private Double total; // price * quantity
}

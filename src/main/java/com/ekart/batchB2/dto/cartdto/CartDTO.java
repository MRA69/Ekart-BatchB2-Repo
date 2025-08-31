package com.ekart.batchB2.dto.cartdto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    private String userId;
    private List<CartItemResDTO> items;
    private Double totalAmount;
}

package com.ekart.batchB2.mapper;

import com.ekart.batchB2.dto.cartdto.CartDTO;
import com.ekart.batchB2.dto.cartdto.CartItemResDTO;
import com.ekart.batchB2.entity.Cart;
import com.ekart.batchB2.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public CartDTO prepareCartDTO(Cart cart){
        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(cart.getUserId());
        List<CartItemResDTO> cartitem = cart.getItems().stream().map(this::prepareCartItemDTO).toList();
        cartDTO.setItems(cartitem);
        cartDTO.setTotalAmount(cart.getTotalAmount());
        return cartDTO;
    }

    public CartItemResDTO prepareCartItemDTO(CartItem cartItem){
        CartItemResDTO cartItemDTO = new CartItemResDTO();
        cartItemDTO.setProductName(cartItem.getProductName());
        cartItemDTO.setPrice(cartItem.getPrice());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setTotal(cartItem.getTotal());
        return cartItemDTO;
    }

    public Cart prepareCartEntity(CartDTO cartDTO){
        Cart cart = new Cart();
        cart.setUserId(cartDTO.getUserId());
        List<CartItem> cartitem = cartDTO.getItems().stream().map(this::prepareCartItemEntity).toList();
        cart.setItems(cartitem);
        return cart;
    }

    public CartItem prepareCartItemEntity(CartItemResDTO cartItemDTO){
        CartItem cartItem = new CartItem();
        cartItem.setProductName(cartItemDTO.getProductName());
        cartItem.setPrice(cartItemDTO.getPrice());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setTotal(cartItemDTO.getTotal());
        return cartItem;
    }
}

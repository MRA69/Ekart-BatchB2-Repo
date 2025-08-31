package com.ekart.batchB2.service;

import com.ekart.batchB2.dto.cartdto.CartDTO;
import com.ekart.batchB2.dto.cartdto.CartEntryDTO;
import com.ekart.batchB2.dto.cartdto.CartReqDTO;
import com.ekart.batchB2.exceptionhandler.CartNotFoundException;
import com.ekart.batchB2.exceptionhandler.ProductNotFoundExcption;
import com.ekart.batchB2.exceptionhandler.UserNotFoundException;

import java.util.List;

public interface CartService {

    public CartDTO getCartByUserId(String userId) throws CartNotFoundException, UserNotFoundException;
    public CartDTO updateCart(String userId, CartReqDTO cartReqDTO) throws CartNotFoundException, UserNotFoundException, ProductNotFoundExcption;
    public CartDTO addToCart(String userId, CartEntryDTO cartEntryDTO) throws CartNotFoundException, UserNotFoundException, ProductNotFoundExcption;
    public CartDTO removeFromCart(String userId, String productId) throws CartNotFoundException, UserNotFoundException, ProductNotFoundExcption;
    public String clearCart(String userId) throws CartNotFoundException, UserNotFoundException, ProductNotFoundExcption;
}

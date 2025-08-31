package com.ekart.batchB2.controller;

import com.ekart.batchB2.dto.cartdto.CartDTO;
import com.ekart.batchB2.dto.cartdto.CartEntryDTO;
import com.ekart.batchB2.dto.cartdto.CartReqDTO;
import com.ekart.batchB2.exceptionhandler.CartNotFoundException;
import com.ekart.batchB2.exceptionhandler.ProductNotFoundExcption;
import com.ekart.batchB2.exceptionhandler.UserNotFoundException;
import com.ekart.batchB2.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@Validated
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping(value = "/{userId}",produces = "application/json")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable String userId) throws CartNotFoundException, UserNotFoundException {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping(value = "/{userId}",consumes = "application/json",produces = "application/json")
    public ResponseEntity<CartDTO> addToCart(@PathVariable String userId, @RequestBody CartEntryDTO cartEntryDTO) throws CartNotFoundException, UserNotFoundException, ProductNotFoundExcption {
        return ResponseEntity.ok(cartService.addToCart(userId, cartEntryDTO));
    }

    @PutMapping(value = "/{userId}",consumes = "application/json",produces = "application/json")
    public ResponseEntity<CartDTO> updateCart(@PathVariable String userId, @RequestBody CartReqDTO cartReqDTO) throws CartNotFoundException, UserNotFoundException, ProductNotFoundExcption {
        return ResponseEntity.ok(cartService.updateCart(userId, cartReqDTO));
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable String userId) throws CartNotFoundException, UserNotFoundException, ProductNotFoundExcption {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }

    @DeleteMapping(value = "/{userId}/{productId}")
    public ResponseEntity<CartDTO> removeFromCart(@PathVariable String userId, @PathVariable String productId) throws CartNotFoundException, UserNotFoundException, ProductNotFoundExcption {
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }
}

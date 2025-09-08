package com.ekart.batchB2.service.impl;

import com.ekart.batchB2.dto.cartdto.CartDTO;
import com.ekart.batchB2.dto.cartdto.CartEntryDTO;
import com.ekart.batchB2.dto.cartdto.CartReqDTO;
import com.ekart.batchB2.entity.Cart;
import com.ekart.batchB2.entity.CartItem;
import com.ekart.batchB2.entity.Product;
import com.ekart.batchB2.exceptionhandler.CartNotFoundException;
import com.ekart.batchB2.exceptionhandler.ProductNotFoundExcption;
import com.ekart.batchB2.exceptionhandler.UserNotFoundException;
import com.ekart.batchB2.mapper.CartMapper;
import com.ekart.batchB2.repository.CartRepository;
import com.ekart.batchB2.repository.ProductRepository;
import com.ekart.batchB2.repository.UserRepository;
import com.ekart.batchB2.service.CartService;
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
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartMapper cartMapper;

    @Override
    public CartDTO getCartByUserId(String userId) throws UserNotFoundException, CartNotFoundException {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (!userRepository.existsByEmail(userId)) {
            logger.warn("User with email " + userId + " not found");
            throw new UserNotFoundException("User with email " + userId + " not found");
        }
        if (!cartOptional.isPresent()) {
            logger.warn("No cart found for user with email " + userId);
            throw new CartNotFoundException("Cart not found for user with email " + userId);
        }
        Cart cart = cartOptional.get();
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            logger.warn("Cart is empty for user with email " + userId);
            throw new CartNotFoundException("Cart is empty for user with email " + userId);
        }

        logger.info("Retrieved most recent cart for user with email {} details {}", userId, cart);
        return cartMapper.prepareCartDTO(cart);
    }

    @Override

    public CartDTO addToCart(String userId, CartEntryDTO cartEntryDTO) throws CartNotFoundException, UserNotFoundException, ProductNotFoundExcption {
        if (!userRepository.existsByEmail(userId)) {
            logger.warn("User with email " + userId + " not found");
            throw new UserNotFoundException("User with email " + userId + " not found");
        }

        Product product = productRepository.findByName(cartEntryDTO.getItem().getProductName());
        if (product == null) {
            logger.warn("Product not found for product name " + cartEntryDTO.getItem().getProductName());
            throw new ProductNotFoundExcption("Product not found for product name " + cartEntryDTO.getItem().getProductName());
        }

        // Get or create the user's cart
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        Cart cart = cartOptional.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setItems(new ArrayList<>());
            return cartRepository.save(newCart);
        });

        // Check if product already exists in cart
        boolean productExists = false;
        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getProductName().equals(product.getName())) {
                cartItem.setQuantity(cartItem.getQuantity() + cartEntryDTO.getItem().getQuantity());
                cartItem.setTotal(cartItem.getQuantity() * cartItem.getPrice());
                logger.info("Updated quantity for product " + cartItem.getProductName() + " in cart");
                productExists = true;
                break;
            }
        }

        // If product doesn't exist in cart, add new cart item
        if (!productExists) {
            CartItem newCartItem = new CartItem();
            newCartItem.setProductName(product.getName());
            newCartItem.setPrice(product.getPrice());
            newCartItem.setQuantity(cartEntryDTO.getItem().getQuantity());
            newCartItem.setTotal(newCartItem.getQuantity() * newCartItem.getPrice());

            if (cart.getItems() == null) {
                cart.setItems(new ArrayList<>());
            }
            cart.getItems().add(newCartItem);
            logger.info("Added new product " + newCartItem.getProductName() + " to cart");
        }

        // Update cart total
        cart.setTotalAmount(cart.getItems().stream()
                .mapToDouble(CartItem::getTotal)
                .sum());

        // Save the updated cart
        cart = cartRepository.save(cart);
        return cartMapper.prepareCartDTO(cart);
    }

    @Override
    public CartDTO updateCart(String userId, CartReqDTO cartReqDTO) throws CartNotFoundException, UserNotFoundException, ProductNotFoundExcption {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (!userRepository.existsByEmail(userId)) {
            logger.warn("User with email " + userId + " not found");
            throw new UserNotFoundException("User with email " + userId + " not found");
        }
        if (!cartOptional.isPresent()) {
            logger.warn("Cart not found for user with email " + userId);
            throw new CartNotFoundException("Cart not found for user with email " + userId);
        }
        Product product = productRepository.findByName(cartReqDTO.getProductName());
        if(product == null){
            logger.warn("Product not found for product name " + cartReqDTO.getProductName());
            throw new ProductNotFoundExcption("Product not found for product name " + cartReqDTO.getProductName());
        }
        else{
            for(CartItem cartItem : cartOptional.get().getItems()){
                if(cartItem.getProductName().equals(cartReqDTO.getProductName())){
                    cartItem.setQuantity(cartItem.getQuantity() + cartReqDTO.getQuantity());
                    cartItem.setTotal(cartItem.getQuantity() * cartItem.getPrice());
                    logger.info("Product " + cartItem.getProductName() + " updated in cart item successfully");
                    cartOptional.get().setItems(cartOptional.get().getItems());
                    cartOptional.get().setTotalAmount(cartOptional.get().getItems().stream()
                            .mapToDouble(CartItem::getTotal)
                            .sum());
                    logger.info("Cart updated successfully");
                    cartRepository.save(cartOptional.get());
                }
            }
            boolean productFound = cartOptional.get().getItems().stream()
                .anyMatch(cartItem -> cartItem.getProductName().equals(cartReqDTO.getProductName()));
            
            if (!productFound) {
                logger.info("Product {} not found in cart", cartReqDTO.getProductName());
                throw new ProductNotFoundExcption("Product " + cartReqDTO.getProductName() + " not found in cart");
            }
            return cartMapper.prepareCartDTO(cartOptional.get());
        }
    }

    @Override
    public CartDTO removeFromCart(String userId, String productId) throws CartNotFoundException, UserNotFoundException, ProductNotFoundExcption {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (!userRepository.existsByEmail(userId)) {
            logger.warn("User with email " + userId + " not found");
            throw new UserNotFoundException("User with email " + userId + " not found");
        }
        if (!cartOptional.isPresent()) {
            logger.warn("Cart not found for user with email " + userId);
            throw new CartNotFoundException("Cart not found for user with email " + userId);
        }
        Product product = productRepository.findByName(productId);
        if(product == null){
            logger.warn("Product not found for product name " + productId);
            throw new ProductNotFoundExcption("Product not found for product name " + productId);
        }
        else{
            for(CartItem cartItem : cartOptional.get().getItems()){
                if(cartItem.getProductName().equals(productId)){
                    logger.info("Product " + cartItem.getProductName() + " removed from cart successfully");
                    cartOptional.get().setItems(cartOptional.get().getItems().stream()
                            .filter(item -> !item.getProductName().equals(productId))
                            .collect(Collectors.toList()));
                    cartOptional.get().setTotalAmount(cartOptional.get().getItems().stream()
                            .mapToDouble(CartItem::getTotal)
                            .sum());
                    cartRepository.save(cartOptional.get());
                    logger.info("Cart updated successfully after deletion of product {}", productId);
                }
            }
            return cartMapper.prepareCartDTO(cartOptional.get());
        }
    }

    @Override
    public String clearCart(String userId) throws CartNotFoundException, UserNotFoundException {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (!userRepository.existsByEmail(userId)) {
            logger.warn("User with email " + userId + " not found");
            throw new UserNotFoundException("User with email " + userId + " not found");
        }
        if (!cartOptional.isPresent()) {
            logger.warn("Cart not found for user with email " + userId);
            throw new CartNotFoundException("Cart not found for user with email " + userId);
        }
        List<CartItem> cartItems = cartOptional.get().getItems();
        logger.info("Cart items cleared successfully and cart values is {}", cartItems);
        cartOptional.get().setItems(new ArrayList<>());
        cartRepository.save(cartOptional.get());
        logger.info("Cart cleared successfully");
        return "Cart cleared successfully";
    }
}

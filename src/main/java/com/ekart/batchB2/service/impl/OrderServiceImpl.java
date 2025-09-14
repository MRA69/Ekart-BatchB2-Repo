package com.ekart.batchB2.service.impl;

import com.ekart.batchB2.dto.orderdto.OrderDTO;
import com.ekart.batchB2.dto.orderdto.OrderEntryDTO;
import com.ekart.batchB2.entity.*;
import com.ekart.batchB2.exceptionhandler.CartNotFoundException;
import com.ekart.batchB2.exceptionhandler.OrderNotFoundException;
import com.ekart.batchB2.exceptionhandler.ProductNotFoundExcption;
import com.ekart.batchB2.exceptionhandler.UserNotFoundException;
import com.ekart.batchB2.mapper.OrderMapper;
import com.ekart.batchB2.repository.CartRepository;
import com.ekart.batchB2.repository.OrderRepository;
import com.ekart.batchB2.repository.ProductRepository;
import com.ekart.batchB2.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrderMapper orderMapper;

    @Override
    public OrderDTO createOrder(String userId) throws CartNotFoundException, ProductNotFoundExcption {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if(cartOptional.isEmpty()){
            logger.warn("Cart not found for user with email " + userId);
            throw new CartNotFoundException("Cart not found for user with email " + userId);
        }
        List<CartItem> cartItems = cartOptional.get().getItems();
        List<Item> orderEntries = new ArrayList<>();
        double totalAmount = 0;
        for(CartItem item : cartItems){
            Product product = productRepository.findByName(item.getProductName());
            if(product == null){
                logger.warn("Product not found for product name " + item.getProductName());
                throw new ProductNotFoundExcption("Product not found for product name " + item.getProductName());
            }
            if(product.getStockQuantity() < item.getQuantity()){
                logger.warn("Product " + item.getProductName() + " is out of stock");
                throw new ProductNotFoundExcption("Product " + item.getProductName() + " is out of stock");
            }
            totalAmount += product.getPrice() * item.getQuantity();
            orderEntries.add(new Item(product.getName(), item.getQuantity()));
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        Order newOrder = new Order();
        newOrder.setUserId(userId);
        newOrder.setItems(orderEntries);
        newOrder.setStatus("Pending");
        newOrder.setTotalAmount(totalAmount);
        newOrder.setCreatedAt(new Date());

        Order savedOrder = orderRepository.save(newOrder);
        logger.info("Order created successfully");
        cartOptional.get().setItems(new ArrayList<>());
        cartRepository.save(cartOptional.get());
        logger.info("Cart cleared successfully");
        return orderMapper.prepareOrderDTO(savedOrder);
    }

    @Override
    public OrderDTO getOrderByUserId(String userId) throws OrderNotFoundException{
        Order orderO = orderRepository.findByUserId(userId);
        if(orderO == null){
            logger.warn("Order not found for user with email " + userId);
            throw new OrderNotFoundException("Order not found for user with email " + userId);
        }
        return orderMapper.prepareOrderDTO(orderO);
    }

    @Override
    public List<OrderDTO> getAllOrders() throws OrderNotFoundException{
        List<Order> orders = orderRepository.findAll();
        if(orders.isEmpty()){
            logger.warn("No orders found");
            throw new OrderNotFoundException("No orders found");
        }
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(orderMapper.prepareOrderDTO(order));
        }
        logger.info("All orders fetched successfully");
        return orderDTOs;
    }
}

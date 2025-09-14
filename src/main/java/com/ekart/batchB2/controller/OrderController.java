package com.ekart.batchB2.controller;

import com.ekart.batchB2.dto.orderdto.OrderDTO;
import com.ekart.batchB2.exceptionhandler.CartNotFoundException;
import com.ekart.batchB2.exceptionhandler.OrderNotFoundException;
import com.ekart.batchB2.exceptionhandler.ProductNotFoundExcption;
import com.ekart.batchB2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Validated
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping(value = "/create/{userId}")
    public ResponseEntity<OrderDTO> createOrder(@PathVariable String userId) throws CartNotFoundException, ProductNotFoundExcption {
        OrderDTO orderDTO = orderService.createOrder(userId);
        return ResponseEntity.ok(orderDTO);
    }
    @GetMapping(value = "/get/{userId}")
    public ResponseEntity<OrderDTO> getOrderByUserId(@PathVariable String userId) throws OrderNotFoundException {
        OrderDTO orderDTO = orderService.getOrderByUserId(userId);
        return ResponseEntity.ok(orderDTO);
    }
    @GetMapping(value = "/getAll")
    public ResponseEntity<List<OrderDTO>> getAllOrders() throws OrderNotFoundException {
        List<OrderDTO> orderDTOs = orderService.getAllOrders();
        return ResponseEntity.ok(orderDTOs);
    }
}

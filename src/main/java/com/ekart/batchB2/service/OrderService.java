package com.ekart.batchB2.service;

import com.ekart.batchB2.dto.orderdto.OrderDTO;
import com.ekart.batchB2.dto.orderdto.OrderEntryDTO;
import com.ekart.batchB2.exceptionhandler.CartNotFoundException;
import com.ekart.batchB2.exceptionhandler.OrderNotFoundException;
import com.ekart.batchB2.exceptionhandler.ProductNotFoundExcption;
import com.ekart.batchB2.exceptionhandler.UserNotFoundException;

import java.util.List;

public interface OrderService {

    public OrderDTO createOrder(String userId) throws CartNotFoundException, ProductNotFoundExcption;
    public OrderDTO getOrderByUserId(String userId) throws OrderNotFoundException;
    public List<OrderDTO> getAllOrders() throws OrderNotFoundException;
}

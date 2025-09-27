package com.ekart.batchB2.mapper;

import com.ekart.batchB2.dto.orderdto.OrderDTO;
import com.ekart.batchB2.dto.orderdto.OrderItemDTO;
import com.ekart.batchB2.entity.Item;
import com.ekart.batchB2.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    public OrderDTO prepareOrderDTO(Order order){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(order.getUserId());
        List<OrderItemDTO> orderitem = order.getItems().stream().map(this::prepareOrderItemDTO).toList();
        orderDTO.setItems(orderitem);
        orderDTO.setStatus(order.getStatus());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setCreatedAt(order.getCreatedAt());
        return orderDTO;
    }
    public Order prepareOrderEntity(OrderDTO orderDTO){
        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        List<Item> orderitem = orderDTO.getItems().stream().map(this::prepareOrderItemEntity).toList();
        order.setItems(orderitem);
        order.setStatus(orderDTO.getStatus());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setCreatedAt(orderDTO.getCreatedAt());
        return order;
    }
    public OrderItemDTO prepareOrderItemDTO(Item item){
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(item.getProductId());
        orderItemDTO.setQuantity(item.getQuantity());
        return orderItemDTO;
    }
    public Item prepareOrderItemEntity(OrderItemDTO orderItemDTO){
        Item orderItem = new Item();
        orderItem.setProductId(orderItemDTO.getProductId());
        orderItem.setQuantity(orderItemDTO.getQuantity());
        return orderItem;
    }
}

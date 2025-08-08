package com.ekart.batchB2.dto;

import com.ekart.batchB2.entity.Order;
import org.bson.types.ObjectId;
import org.stringtemplate.v4.ST;

import java.util.Date;
import java.util.List;

public class OrderDTO {

    private String id;

    private ObjectId userId;

    private List<ItemDTO> items;

    private double totalAmount;

    private String status; // e.g., "Pending", "Shipped", "Delivered"

    private Date createdAt;

    // Constructors
    public OrderDTO() {}

    public OrderDTO(String id, ObjectId userId, List<ItemDTO> items, double totalAmount, String status, Date createdAt) {
        super();
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // Nested Item class
    public static class ItemDTO {
        private ObjectId productId;
        private int quantity;

        public ItemDTO() {}

        public ItemDTO(ObjectId productId, int quantity) {
            super();
            this.productId = productId;
            this.quantity = quantity;
        }

        public ObjectId getProductId() {
            return productId;
        }

        public void setProductId(ObjectId productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static Order prepareOrderEntity(OrderDTO orderDTO) {
        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setItems(orderDTO.getItems().stream()
                .map(item -> new Order.Item(item.getProductId(), item.getQuantity()))
                .toList());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setStatus(orderDTO.getStatus());
        order.setCreatedAt(orderDTO.getCreatedAt());
        return order;
    }
}

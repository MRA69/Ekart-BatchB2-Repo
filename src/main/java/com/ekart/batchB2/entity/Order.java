package com.ekart.batchB2.entity;

import com.ekart.batchB2.dto.OrderDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private ObjectId userId;

    private List<Item> items;

    private double totalAmount;

    private String status; // e.g., "Pending", "Shipped", "Delivered"

    private Date createdAt;

    // Constructors
    public Order() {}

    public Order(ObjectId userId, List<Item> items, double totalAmount, String status, Date createdAt) {
        super();
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

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
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
    public static class Item {
        private ObjectId productId;
        private int quantity;

        public Item() {}

        public Item(ObjectId productId, int quantity) {
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

    public static OrderDTO prepareOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUserId(order.getUserId());

        List<OrderDTO.ItemDTO> itemDTOs = order.getItems().stream()
            .map(item -> new OrderDTO.ItemDTO(item.getProductId(), item.getQuantity()))
            .toList();

        orderDTO.setItems(itemDTOs);
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setCreatedAt(order.getCreatedAt());

        return orderDTO;
    }
}

package com.ekart.batchB2.entity;

import com.ekart.batchB2.dto.CartDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

import java.util.List;

@Document(collection = "cart")
public class Cart {

    @Id
    private String id;

    private ObjectId userId;

    private List<Item> items;

    // Constructors
    public Cart() {}

    public Cart(ObjectId userId, List<Item> items) {
        super();
        this.userId = userId;
        this.items = items;
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

    public static CartDTO prepareCartDTO(Cart cart) {
        List<Item> items = cart.getItems();
        List<CartDTO.ItemDTO> itemDTOs = items.stream()
                .map(item -> new CartDTO.ItemDTO(item.getProductId(), item.getQuantity()))
                .toList();
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getUserId());
        cartDTO.setItems(itemDTOs);
        return cartDTO;
    }
}

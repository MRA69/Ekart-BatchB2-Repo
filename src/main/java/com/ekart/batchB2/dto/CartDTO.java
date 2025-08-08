package com.ekart.batchB2.dto;

import com.ekart.batchB2.entity.Cart;
import org.bson.types.ObjectId;

import java.util.List;

public class CartDTO {

    private String id;

    private ObjectId userId;

    private List<CartDTO.ItemDTO> items;

    // Constructors
    public CartDTO() {}

    public CartDTO(String id, ObjectId userId, List<CartDTO.ItemDTO> items) {
        super();
        this.id = id;
        this.userId = userId;
        this.items = items;
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

    public List<CartDTO.ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartDTO.ItemDTO> items) {
        this.items = items;
    }

    // Nested Item class
    public static class ItemDTO {
        private ObjectId productId;
        private int quantity;

        public ItemDTO() {}

        public ItemDTO(ObjectId productId, int quantity) {
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

    public static Cart prepareCartEntity(CartDTO cartDTO) {
        Cart cart = new Cart();
        cart.setUserId(cartDTO.getUserId());
        List<Cart.Item> items = cartDTO.getItems().stream()
                .map(itemDTO -> new Cart.Item(itemDTO.getProductId(), itemDTO.getQuantity()))
                .toList();

        cart.setItems(items);
        return cart;
    }
}

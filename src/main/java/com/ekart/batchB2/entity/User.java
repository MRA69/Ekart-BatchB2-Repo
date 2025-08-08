package com.ekart.batchB2.entity;

import com.ekart.batchB2.dto.UserDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private String password; // Store as hashed string
    @DBRef
    private List<Address> addresses;
    @DBRef
    private List<Order> orders;

    // Constructors
    public User() {}

    public User(String username, String email, String password, List<Address> addresses, List<Order> orders) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.addresses = addresses;
        this.orders = orders;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public static UserDTO prepareUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setAddresses(user.getAddresses());
        userDTO.setOrders(user.getOrders());
        return userDTO;
    }
}

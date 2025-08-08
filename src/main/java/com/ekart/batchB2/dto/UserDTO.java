package com.ekart.batchB2.dto;

import com.ekart.batchB2.entity.Address;
import com.ekart.batchB2.entity.Order;
import com.ekart.batchB2.entity.User;
import jakarta.validation.constraints.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

public class UserDTO {

    private String id;
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
             message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;
    @NotEmpty(message = "Addresses cannot be empty")
    @Size(min = 1, message = "At least one address is required")
    private List<Address> addresses;
    private List<Order> orders;

    // Constructors
    public UserDTO() {}

    public UserDTO(String username, String email, String password, List<Address> addresses, List<Order> orders) {
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

    public void setId(String id) {
        this.id = id;
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

    public static User prepareUserEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAddresses(userDTO.getAddresses());
        user.setOrders(userDTO.getOrders());
        return user;
    }
}

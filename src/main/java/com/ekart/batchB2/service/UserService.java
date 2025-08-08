package com.ekart.batchB2.service;

import com.ekart.batchB2.dto.UserDTO;

import java.util.List;

public interface UserService {
    // Define methods that the UserService should implement
    String createUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    String updateUserPass(String email, String newPassword);
    String deleteUser(String email);
    boolean userLogin(String email, String password);
}

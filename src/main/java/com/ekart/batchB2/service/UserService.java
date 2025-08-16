package com.ekart.batchB2.service;

import com.ekart.batchB2.dto.UserDTO;
import com.ekart.batchB2.exceptionhandler.DuplicateUserException;
import com.ekart.batchB2.exceptionhandler.UserNotFoundException;

import java.util.List;

public interface UserService {
    // Define methods that the UserService should implement
    public String createUser(UserDTO userDTO) throws DuplicateUserException;
    public List<UserDTO> getAllUsers();
    public String updateUserPass(String email, String newPassword) throws UserNotFoundException;
    public String deleteUser(String email) throws UserNotFoundException;
    public boolean userLogin(String email, String password) throws UserNotFoundException;
}

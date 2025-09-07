package com.ekart.batchB2.service;

import com.ekart.batchB2.dto.AddressDTO;
import com.ekart.batchB2.dto.UserDTO;
import com.ekart.batchB2.exceptionhandler.AddressOperationException;
import com.ekart.batchB2.exceptionhandler.DuplicateUserException;
import com.ekart.batchB2.exceptionhandler.UserNotFoundException;

import java.util.List;

public interface UserService {
    // Define methods that the UserService should implement
    String createUser(UserDTO userDTO) throws DuplicateUserException;
    
    String createAddressAndupdateAddress(String email, AddressDTO addressDTO) 
            throws UserNotFoundException, AddressOperationException;
            
    List<UserDTO> getAllUsers();
    
    String updateUserPass(String email, String newPassword) throws UserNotFoundException;
    
    String setAddressDefault(String email, String addressId) throws UserNotFoundException;
    
    String deleteUser(String email) throws UserNotFoundException;
    
    String removeAddress(String email, String addressId) throws UserNotFoundException;
    
    boolean userLogin(String email, String password) throws UserNotFoundException;
    
    UserDTO getUserByEmail(String email) throws UserNotFoundException;
}

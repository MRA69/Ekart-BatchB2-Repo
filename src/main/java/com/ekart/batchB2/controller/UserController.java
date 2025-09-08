package com.ekart.batchB2.controller;

import com.ekart.batchB2.dto.*;
import com.ekart.batchB2.exceptionhandler.AddressOperation;
import com.ekart.batchB2.exceptionhandler.DuplicateUserException;
import com.ekart.batchB2.exceptionhandler.UserNotFoundException;
import com.ekart.batchB2.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Validated
@RequestMapping("/user")
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDTO userDTO) throws DuplicateUserException {
        String response = userService.createUser(userDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<Boolean> userLogin(@RequestBody @Valid LoginDTO loginDTO) throws UserNotFoundException {
        boolean isValidUser = userService.userLogin(loginDTO.getEmail(), loginDTO.getPassword());
        return ResponseEntity.ok(isValidUser);
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/getByEmail/{email}", produces = "application/json")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable ("email") String email) throws UserNotFoundException {
        UserDTO userDTO = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDTO);
    }


    @PutMapping(value = "/updatePass")
    public ResponseEntity<String> updateUserPass(@RequestParam (value = "email") String email, @RequestParam (value = "newPassword") String newPassword)
            throws UserNotFoundException {
        String response = userService.updateUserPass(email, newPassword);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/addAddress/{email}", consumes = "application/json")
    public ResponseEntity<String> addAddress(
            @PathVariable("email") String email, 
            @RequestBody @Valid AddressDTO addressDTO) throws AddressOperation, UserNotFoundException {
            String message = userService.createAddressAndupdateAddress(email, addressDTO);
            return ResponseEntity.ok(message);
    }

    @PatchMapping(value = "/setDefaultAddress")
    public ResponseEntity<String> setAddressDefault(@RequestParam (value = "email") String email, @RequestParam (value = "addressId") String addressId) throws UserNotFoundException {
        String response = userService.setAddressDefault(email, addressId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/removeAddress/{email}")
    public ResponseEntity<String> removeAddress(@PathVariable ("email") String email, @RequestParam ("addressId") String addressId) throws UserNotFoundException {
        String response = userService.removeAddress(email, addressId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteUser(@RequestParam (value = "email") String email) throws UserNotFoundException {
        String response = userService.deleteUser(email);
        return ResponseEntity.ok(response);
    }
}

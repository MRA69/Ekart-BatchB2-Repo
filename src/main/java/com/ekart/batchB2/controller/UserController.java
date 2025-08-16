package com.ekart.batchB2.controller;

import com.ekart.batchB2.dto.LoginDTO;
import com.ekart.batchB2.exceptionhandler.DuplicateUserException;
import com.ekart.batchB2.exceptionhandler.UserNotFoundException;
import com.ekart.batchB2.service.UserService;
import jakarta.validation.Valid;
import com.ekart.batchB2.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDTO userDTO) throws DuplicateUserException {
        String response = userService.createUser(userDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Boolean> userLogin(@RequestBody @Valid LoginDTO loginDTO) throws UserNotFoundException {
        boolean isValidUser = userService.userLogin(loginDTO.getEmail(), loginDTO.getPassword());
        return ResponseEntity.ok(isValidUser);
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @PutMapping(value = "/updatePass")
    public ResponseEntity<String> updateUserPass(@RequestParam (value = "email") String email, @RequestParam (value = "newPassword") String newPassword)
            throws UserNotFoundException {
        String response = userService.updateUserPass(email, newPassword);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteUser(@RequestParam (value = "email") String email) throws UserNotFoundException {
        String response = userService.deleteUser(email);
        return ResponseEntity.ok(response);
    }
}

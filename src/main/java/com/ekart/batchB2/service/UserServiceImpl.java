package com.ekart.batchB2.service;

import com.ekart.batchB2.dto.UserDTO;
import com.ekart.batchB2.entity.User;
import com.ekart.batchB2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String createUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return "User already exists";
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(UserDTO.prepareUserEntity(userDTO));
        return "User "+userDTO.getUsername()+" created successfully";
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTO = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            userDTO.add(User.prepareUserDTO(user));
        });
        return userDTO;
    }

    @Override
    public String updateUserPass(String email, String newPassword) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return "User with "+email+" not found please check the email";
        }
        user.get().setPassword(passwordEncoder.encode(newPassword)); // Ideally, hash the password before saving
        userRepository.save(user.get());
        return "Password updated successfully";
    }

    @Override
    public String deleteUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return "User with "+email+" not found please check the email";
        }
        userRepository.delete(user.get());
        return "User "+user.get().getUsername()+" deleted successfully";
    }

    @Override
    public boolean userLogin(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(value -> passwordEncoder.matches(password, value.getPassword())).orElse(false);
        // Ideally, compare hashed passwords
    }
}

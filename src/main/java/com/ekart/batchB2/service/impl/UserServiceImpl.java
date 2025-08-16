package com.ekart.batchB2.service.impl;

import com.ekart.batchB2.dto.UserDTO;
import com.ekart.batchB2.entity.Address;
import com.ekart.batchB2.entity.User;
import com.ekart.batchB2.exceptionhandler.DuplicateUserException;
import com.ekart.batchB2.exceptionhandler.ExceptionHandlerClass;
import com.ekart.batchB2.exceptionhandler.UnauthorizedAccessException;
import com.ekart.batchB2.exceptionhandler.UserNotFoundException;
import com.ekart.batchB2.mapper.UserMapper;
import com.ekart.batchB2.repository.AddressRepository;
import com.ekart.batchB2.repository.UserRepository;
import com.ekart.batchB2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserMapper userMapper;

    @Override
    public String createUser(UserDTO userDTO) throws DuplicateUserException {
        logger.info("Creating a new user with email: {}", userDTO.getEmail());
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            logger.warn("User with email {} already exists", userDTO.getEmail());
            throw new DuplicateUserException("User with email " + userDTO.getEmail() + " already exists");
        }

        List<Address> savedAddresses = userDTO.getAddresses().stream()
                .map(address -> addressRepository.save(userMapper.prepareAddressEntity(address)))
                .collect(Collectors.toList());

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userRepository.save(userMapper.prepareUserEntity(userDTO));
        user.setAddresses(savedAddresses);
        return "User "+userDTO.getUsername()+" created successfully";
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(userMapper.prepareUserDTO(user));
        }
        return userDTOs;
    }

    @Override
    public String updateUserPass(String email, String newPassword) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            logger.warn("User with email " + email + " not found");
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        user.get().setPassword(passwordEncoder.encode(newPassword)); // Ideally, hash the password before saving
        userRepository.save(user.get());
        return "Password updated successfully";
    }

    @Override
    public String deleteUser(String email) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            logger.warn("User with email " + email + " not found");
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        userRepository.delete(user.get());
        return "User "+user.get().getUsername()+" deleted successfully";
    }

    @Override
    public boolean userLogin(String email, String password) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            logger.warn("User with email " + email + " not found");
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        return user.map(value -> passwordEncoder.matches(password, value.getPassword())).orElse(false);
    }
}

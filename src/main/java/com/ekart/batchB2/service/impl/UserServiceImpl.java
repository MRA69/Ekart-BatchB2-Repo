package com.ekart.batchB2.service.impl;

import com.ekart.batchB2.dto.AddressDTO;
import com.ekart.batchB2.dto.UserDTO;
import com.ekart.batchB2.entity.Address;
import com.ekart.batchB2.entity.User;
import com.ekart.batchB2.exceptionhandler.AddressOperation;
import com.ekart.batchB2.exceptionhandler.DuplicateUserException;
import com.ekart.batchB2.exceptionhandler.UserNotFoundException;
import com.ekart.batchB2.mapper.UserMapper;
import com.ekart.batchB2.repository.UserRepository;
import com.ekart.batchB2.service.UserService;
import com.ekart.batchB2.util.AddressType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Override
    public String createUser(UserDTO userDTO) throws DuplicateUserException {
        logger.info("Creating a new user with email: {}", userDTO.getEmail());
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            logger.warn("User with email {} already exists", userDTO.getEmail());
            throw new DuplicateUserException("User with email " + userDTO.getEmail() + " already exists");
        }

        List<AddressDTO> savedAddresses = userDTO.getAddresses();

        userDTO.getAddresses().forEach(addressDTO -> {
            addressDTO.setId(UUID.randomUUID().toString());
        });
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setAddresses(savedAddresses);
        userRepository.save(userMapper.prepareUserEntity(userDTO));
        logger.info("User created successfully with email: {}", userDTO.getEmail());
        return "User "+userDTO.getUsername()+" created successfully";
    }

    @Override
    @Transactional
    public String createAddressAndupdateAddress(String email, AddressDTO addressDTO) 
            throws UserNotFoundException, AddressOperation {
        
        logger.info("Processing address operation for user: {}", email);

        try {
            // Validate input
            if (addressDTO == null) {
                throw new AddressOperation("Address data cannot be null");
            }
            
            // Find user or throw exception
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
                    
            // Initialize addresses list if null
            List<Address> addresses = Optional.ofNullable(user.getAddresses())
                    .orElseGet(ArrayList::new);
                    
            // Check if this is an update or create operation
            boolean isUpdateOperation = addressDTO.getId() != null && !addressDTO.getId().isEmpty();
            
            // For CUSTOM addresses, check for duplicates before proceeding
            if (addressDTO.getName().equals(AddressType.CUSTOM.name())) {
                boolean duplicateExists = addresses.stream()
                        .filter(addr -> addr.getName().equals(AddressType.CUSTOM.name()))
                        .anyMatch(addr -> addr.getStreet().equalsIgnoreCase(addressDTO.getStreet()) &&
                                        addr.getCity().equalsIgnoreCase(addressDTO.getCity()) &&
                                        addr.getState().equalsIgnoreCase(addressDTO.getState()) &&
                                        addr.getZipCode().equals(addressDTO.getZipCode()) &&
                                        addr.getCountry().equalsIgnoreCase(addressDTO.getCountry()));
                                        
                if (duplicateExists) {
                    throw new AddressOperation("This address already exists");
                }
            }
            
            // Handle update operation
            if (isUpdateOperation) {
                // Find the address to update
                Address existingAddress = addresses.stream()
                        .filter(addr -> addr.getId().equals(addressDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new AddressOperation("Address not found with ID: " + addressDTO.getId()));
                
                // Update address fields
                existingAddress.setName(addressDTO.getName());
                existingAddress.setStreet(addressDTO.getStreet());
                existingAddress.setCity(addressDTO.getCity());
                existingAddress.setState(addressDTO.getState());
                existingAddress.setZipCode(addressDTO.getZipCode());
                existingAddress.setCountry(addressDTO.getCountry());
                
                // Handle default status
                if (Boolean.TRUE.equals(addressDTO.getIsDefault())) {
                    addresses.forEach(addr -> addr.setIsDefault(false));
                    existingAddress.setIsDefault(true);
                } else if (existingAddress.getIsDefault() && 
                          Boolean.FALSE.equals(addressDTO.getIsDefault()) && 
                          !addresses.isEmpty()) {
                    // If unsetting default, set the first address as default
                    addresses.get(0).setIsDefault(true);
                }
                
                user.setAddresses(addresses);
                userRepository.save(user);
                
                logger.info("Address updated successfully for user: {}", email);
                return "Address updated successfully";
            } 
            // Handle create operation
            else {
                // Create new address
                Address newAddress = userMapper.prepareAddressEntity(addressDTO);
                newAddress.setId(UUID.randomUUID().toString());
                
                // Set default to false if null
                if (newAddress.getIsDefault() == null) {
                    newAddress.setIsDefault(false);
                }
                
                // If this is the first address, set it as default
                if (addresses.isEmpty()) {
                    newAddress.setIsDefault(true);
                } 
                // If setting as default, update other addresses
                else if (Boolean.TRUE.equals(newAddress.getIsDefault())) {
                    addresses.forEach(addr -> addr.setIsDefault(false));
                }
                
                // For HOME or WORK type, check if already exists and update instead of adding new
                if (addressDTO.getName().equals(AddressType.HOME.name()) || 
                    addressDTO.getName().equals(AddressType.WORK.name())) {
                    
                    boolean updated = addresses.removeIf(addr -> addr.getName().equals(addressDTO.getName()));
                    if (updated) {
                        logger.info("Updated existing {} address for user: {}", addressDTO.getName(), email);
                    }
                }
                
                // Add the new/updated address
                addresses.add(newAddress);
                user.setAddresses(addresses);
                userRepository.save(user);
                
                logger.info("New address added successfully for user: {}", email);
                return "Address added successfully";
            }
        } catch (UserNotFoundException | AddressOperation e) {
            // Re-throw these exceptions as they are already properly typed
            logger.error("Error processing address operation for user: {}", email, e);
            throw e;
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            String errorMsg = String.format("Failed to process address operation for user %s: %s", 
                    email, e.getMessage());
            logger.error(errorMsg, e);
            throw new AddressOperation("An error occurred while processing your request");
        }
    }

    @Override
    public String setAddressDefault(String email, String addressId) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            logger.warn("User with email " + email + " not found");
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        boolean found = false;
        for (Address addr : user.get().getAddresses()) {
            if (addr.getId().equals(addressId)) {
                addr.setIsDefault(true);
                found = true;
            } else {
                addr.setIsDefault(false);
            }
        }

        if (!found) {
            throw new UserNotFoundException("Address not found for user");
        }
        userRepository.save(user.get());
        logger.info("Address set as default successfully for user with email: {} and address is {}", email, addressId);
        return "Address set as default successfully";
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(userMapper.prepareUserDTO(user));
        }
        logger.info("All users fetched successfully");
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
        logger.info("Password updated successfully for user with email: {}", email);
        return "Password updated successfully";
    }

    @Override
    public UserDTO getUserByEmail(String email) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            logger.warn("User with email " + email + " not found");
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        logger.info("User fetched successfully with email: {}", email);
        return userMapper.prepareUserDTO(user.get());
    }

    @Override
    public String deleteUser(String email) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            logger.warn("User with email " + email + " not found");
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        userRepository.delete(user.get());
        logger.info("User deleted successfully with email: {}", email);
        return "User "+user.get().getUsername()+" deleted successfully";
    }
    @Override
    public String removeAddress(String email, String addressId) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            logger.warn("User with email " + email + " not found");
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        List<Address> addresses = user.get().getAddresses();
        if (addresses == null) {
            logger.warn("No addresses found for user with email " + email);
            throw new UserNotFoundException("No addresses found for user with email " + email);
        }
        boolean checker = addresses.removeIf(address -> address.getId().equals(addressId));
        if(!checker){
            throw new UserNotFoundException("Address not found for user");
        }
        boolean defaultchecker = false;
        for(Address addr : addresses){
            if(addr.getIsDefault() == true){
                defaultchecker = true;
            }
        }
        if(!defaultchecker){
            addresses.getFirst().setIsDefault(true);
        }
        user.get().setAddresses(addresses);
        userRepository.save(user.get());
        logger.info("Address removed successfully for user with email: {}", email);
        return "Address removed successfully";
    }

    @Override
    public boolean userLogin(String email, String password) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            logger.warn("User with email " + email + " not found");
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        logger.info("User login successful for user with email: {}", email);
        return user.map(value -> passwordEncoder.matches(password, value.getPassword())).orElse(false);
    }
}

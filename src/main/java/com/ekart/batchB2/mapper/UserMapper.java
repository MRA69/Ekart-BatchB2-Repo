package com.ekart.batchB2.mapper;

import com.ekart.batchB2.dto.AddressDTO;
import com.ekart.batchB2.dto.UserDTO;
import com.ekart.batchB2.entity.Address;
import com.ekart.batchB2.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public UserDTO prepareUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        List<AddressDTO> addressDTOs = user.getAddresses().stream().map(this::prepareAddressDTO).toList();
        userDTO.setAddresses(addressDTOs);
        return userDTO;
    }
    public User prepareUserEntity(UserDTO userDTO){
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        List<Address> addresses = userDTO.getAddresses().stream().map(this::prepareAddressEntity).toList();
        user.setAddresses(addresses);
        return user;
    }

    public AddressDTO prepareAddressDTO(Address address){
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName(address.getName());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setZipCode(address.getZipCode());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setIsDefault(address.getIsDefault());
        return addressDTO;
    }
    public Address prepareAddressEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setName(addressDTO.getName());
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipCode(addressDTO.getZipCode());
        address.setCountry(addressDTO.getCountry());
        address.setIsDefault(addressDTO.getIsDefault());
        return address;
    }
}

package com.ekart.batchB2.dto;

import com.ekart.batchB2.entity.Address;
import org.bson.types.ObjectId;

public class AddressDTO {

    private String id;

    private ObjectId userId;

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    // Constructors
    public AddressDTO() {}

    public AddressDTO(String id, ObjectId userId, String street, String city, String state, String zipCode, String country) {
        super();
        this.id = id;
        this.userId = userId;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static Address prepareAddressEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setUserId(addressDTO.getUserId());
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipCode(addressDTO.getZipCode());
        address.setCountry(addressDTO.getCountry());
        return address;
    }
}

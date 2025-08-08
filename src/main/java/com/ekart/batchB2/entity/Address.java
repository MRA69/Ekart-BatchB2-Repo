package com.ekart.batchB2.entity;

import com.ekart.batchB2.dto.AddressDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

@Document(collection = "addresses")
public class Address {

    @Id
    private String id;

    private ObjectId userId;

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    // Constructors
    public Address() {}

    public Address(ObjectId userId, String street, String city, String state, String zipCode, String country) {
        super();
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

    public static AddressDTO prepareAddressDTO(Address address) {
        return new AddressDTO(
            address.getId(),
            address.getUserId(),
            address.getStreet(),
            address.getCity(),
            address.getState(),
            address.getZipCode(),
            address.getCountry()
        );
    }
}
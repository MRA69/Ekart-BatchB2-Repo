package com.ekart.batchB2.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String id;
    private String name; // Home, Office, etc.
    private String street;
    private String city;
    private String state;
    private Integer zipCode;
    private String country;
    private Boolean isDefault;
}
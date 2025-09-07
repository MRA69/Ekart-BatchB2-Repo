package com.ekart.batchB2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private String id;
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s\\.'-]+$", message = "Name can only contain letters, spaces, and basic punctuation")
    private String name;

    @NotBlank(message = "Street address is required")
    @Size(max = 200, message = "Street address cannot exceed 200 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City name cannot exceed 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s\\.'-]+$", message = "Invalid city name")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State name cannot exceed 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s\\.'-]+$", message = "Invalid state name")
    private String state;

    @NotNull(message = "ZIP code is required")
    @Min(value = 100000, message = "Zip code must be exactly 6 digits")
    @Max(value = 999999, message = "Zip code must be exactly 6 digits")
    private Integer zipCode;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country name cannot exceed 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s\\.'-]+$", message = "Invalid country name")
    private String country;

    private Boolean isDefault;
}

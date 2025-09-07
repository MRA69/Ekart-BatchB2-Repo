package com.ekart.batchB2.dto;

import com.ekart.batchB2.entity.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
             message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;
    @NotNull(message = "Phone number is required")
    @Min(value = 1000000000L, message = "Phone number must be exactly 10 digits")
    @Max(value = 9999999999L, message = "Phone number must be exactly 10 digits")
    private Long phoneNumber;
    @NotBlank(message = "Role is required")
    @Size(min = 3, max = 20, message = "Role must not exceed 20 characters")
    private String role;
    @NotEmpty(message = "Addresses cannot be empty")
    @Size(min = 1, message = "At least one address is required")
    @Valid
    private List<AddressDTO> addresses;
}

package com.medtrackfit.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;
    @Email(message = "Email address is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    private String password;
    @NotBlank(message = "Phone number is required")
    @Size(min = 8, max = 12, message = "Invalid Phone Number")
    private String phoneNumber;
    @NotBlank(message = "about is required")
    @Size(max = 100, message = "About must be less than 100 characters")
    private String about;
}

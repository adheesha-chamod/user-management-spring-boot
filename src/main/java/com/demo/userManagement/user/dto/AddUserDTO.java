package com.demo.userManagement.user.dto;

import com.demo.userManagement.user.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDTO {

    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
    @NotNull
    private UserType userType;
}

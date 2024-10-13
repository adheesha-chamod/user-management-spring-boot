package com.demo.userManagement.user.dto;

import com.demo.userManagement.user.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDTO {

    private String name;
    private String email;
    private UserType userType;
}

package com.demo.userManagement.user.dto;

import com.demo.userManagement.user.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDetailsDTO {

    private String id;
    private String name;
    private String email;
    private UserType userType;
    private String createdOn;
    private String lastModifiedOn;
}

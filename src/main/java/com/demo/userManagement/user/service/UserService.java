package com.demo.userManagement.user.service;

import com.demo.userManagement.user.dto.AddUserDTO;
import com.demo.userManagement.user.dto.UserDetailsDTO;
import com.demo.userManagement.user.entity.User;
import com.demo.userManagement.user.exception.InvalidAddUserRequestException;
import com.demo.userManagement.user.exception.UserNotFoundException;
import com.demo.userManagement.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserDetailsDTO addUser(AddUserDTO addUserDTO) {
        if (isNameDuplicate(addUserDTO.getName().trim())) {
            log.error(String.format("[addUser] Name '%s' already exists", addUserDTO.getName().trim()));
            throw new InvalidAddUserRequestException("Name already exists");
        }
        if (isEmailDuplicate(addUserDTO.getEmail().trim())) {
            log.error(String.format("[addUser] Email '%s' already exists", addUserDTO.getEmail().trim()));
            throw new InvalidAddUserRequestException("Email already exists");
        }

        User newUser = User.builder()
                .name(addUserDTO.getName().trim())
                .email(addUserDTO.getEmail().trim())
                .userType(addUserDTO.getUserType())
                .build();

        return getUserDetailsDTO(userRepository.save(newUser));
    }

    public UserDetailsDTO getUser(String userId) {
        User user = userRepository.findById(new ObjectId(userId)).orElseGet(() -> {
            log.error(String.format("[getUser] User with id '%s' not found", userId));
            throw new UserNotFoundException(String.format("User with id '%s' not found", userId));
        });
        
        return getUserDetailsDTO(user);
    }

    private UserDetailsDTO getUserDetailsDTO(User user) {
        return UserDetailsDTO.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .userType(user.getUserType())
                .createdOn(user.getCreatedOn().toString())
                .lastModifiedOn(user.getLastModifiedOn().toString())
                .build();
    }

    private boolean isNameDuplicate(String name) {
        return userRepository.existsByNameIgnoreCase(name);
    }

    private boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}

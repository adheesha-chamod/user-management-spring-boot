package com.demo.userManagement.user.controller;

import com.demo.userManagement.user.dto.AddUserDTO;
import com.demo.userManagement.user.dto.UpdateUserDTO;
import com.demo.userManagement.user.dto.UserDetailsDTO;
import com.demo.userManagement.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> addUser(
            @Valid @RequestBody AddUserDTO addUserDTO) {
        String newUserId = userService.addUser(addUserDTO).toString();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newUserId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsDTO> getUser(@PathVariable("userId") String userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUser(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateUser(
            @PathVariable("userId") String userId,
            @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        userService.updateUser(userId, updateUserDTO);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

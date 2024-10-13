package com.demo.userManagement.user.controller;

import com.demo.userManagement.user.dto.AddUserDTO;
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
    public ResponseEntity<UserDetailsDTO> addUser(
            @Valid @RequestBody AddUserDTO addUserDTO) {
        log.debug("Received request to add user: {}", addUserDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.addUser(addUserDTO));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsDTO> getUser(@PathVariable("userId") String userId) {
        log.debug("Received request to get user with id: {}", userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUser(userId));
    }
}

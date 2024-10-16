package com.demo.userManagement.user.service;

import com.demo.userManagement.user.dto.AddUserDTO;
import com.demo.userManagement.user.dto.UpdateUserDTO;
import com.demo.userManagement.user.dto.UserDetailsDTO;
import com.demo.userManagement.user.dto.UserSummaryDTO;
import com.demo.userManagement.user.entity.User;
import com.demo.userManagement.user.exception.InvalidAddUserRequestException;
import com.demo.userManagement.user.exception.UserNotFoundException;
import com.demo.userManagement.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.demo.userManagement.util.ValidationUtil.isValidEmail;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public ObjectId addUser(AddUserDTO addUserDTO) {
        if (isEmailDuplicate(addUserDTO.getEmail().trim())) {
            log.error(String.format("[addUser] Email '%s' already exists", addUserDTO.getEmail().trim()));
            throw new InvalidAddUserRequestException("Email already exists");
        }

        User newUser = User.builder()
                .name(addUserDTO.getName().trim())
                .email(addUserDTO.getEmail().trim())
                .userType(addUserDTO.getUserType())
                .build();

        return userRepository.save(newUser).getId();
    }

    public UserDetailsDTO getUser(String userId) {
        User user = userRepository.findById(new ObjectId(userId))
                .orElseThrow(() -> {
                    log.error(String.format("[getUser] User with id '%s' not found", userId));
                    return new UserNotFoundException(String.format("User with id '%s' not found", userId));
                });
        
        return getUserDetailsDTO(user);
    }

    public Page<UserSummaryDTO> getUsers(String search, Pageable pageable) {
        Query query = new Query().with(pageable);
        if (search != null && !search.isBlank()) {
            // search for both name and email | case-insensitive & prefix-based
            Criteria criteria = new Criteria().orOperator(
                    Criteria.where("name").regex("^" + search.trim(), "i"),
                    Criteria.where("email").regex("^" + search.trim(), "i")
            );
            query.addCriteria(criteria);
        }

        long total = mongoTemplate.count(query, User.class);
        List<User> users = mongoTemplate.find(query, User.class);
        List<UserSummaryDTO> userSummaryDTOs = users.stream()
                .map(this::getUserSummaryDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(userSummaryDTOs, pageable, total);
    }

    public void updateUser(String userId, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(new ObjectId(userId))
                .orElseThrow(() -> {
                    log.error(String.format("[updateUser] User with id '%s' not found", userId));
                    return new UserNotFoundException(String.format("User with id '%s' not found", userId));
                });

        boolean isUpdated = false;

        if (updateUserDTO.getName() != null
                && !updateUserDTO.getName().isBlank()
                && !updateUserDTO.getName().trim().equals(user.getName())) {
            user.setName(updateUserDTO.getName().trim());
            isUpdated = true;
        }
        if (updateUserDTO.getEmail() != null
                && !updateUserDTO.getEmail().isBlank()
                && !updateUserDTO.getEmail().trim().equals(user.getEmail())) {
            if (!isValidEmail(updateUserDTO.getEmail().trim())) {
                log.error(String.format("[updateUser] Invalid email '%s'", updateUserDTO.getEmail().trim()));
                throw new InvalidAddUserRequestException(String.format("Invalid email '%s'", updateUserDTO.getEmail().trim()));
            }
            if (isEmailDuplicate(updateUserDTO.getEmail().trim())) {
                log.error(String.format("[updateUser] Email '%s' already exists", updateUserDTO.getEmail().trim()));
                throw new InvalidAddUserRequestException(String.format("Email '%s' already exists", updateUserDTO.getEmail().trim()));
            }
            user.setEmail(updateUserDTO.getEmail().trim());
            isUpdated = true;
        }
        if (updateUserDTO.getUserType() != null
                && updateUserDTO.getUserType() != user.getUserType()) {
            user.setUserType(updateUserDTO.getUserType());
            isUpdated = true;
        }

        if (isUpdated) {
            userRepository.save(user);
        } else {
            log.warn("[updateUser] No changes detected to update");
        }
    }

    public void deleteUser(String userId) {
        ObjectId id = new ObjectId(userId);
        if (!userRepository.existsById(id)) {
            log.error(String.format("[deleteUser] User with id '%s' not found", userId));
            throw new UserNotFoundException(String.format("User with id '%s' not found", userId));
        }
        userRepository.deleteById(id);
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

    private UserSummaryDTO getUserSummaryDTO(User user) {
        return UserSummaryDTO.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .userType(user.getUserType())
                .build();
    }

    private boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}

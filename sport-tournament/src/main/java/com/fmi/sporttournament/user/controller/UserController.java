package com.fmi.sporttournament.user.controller;

import com.fmi.sporttournament.user.dto.response.UserDto;
import com.fmi.sporttournament.user.dto.request.ChangeRoleRequest;
import com.fmi.sporttournament.user.entity.User;
import com.fmi.sporttournament.user.entity.role.Role;
import com.fmi.sporttournament.user.mapper.UserMapper;
import com.fmi.sporttournament.user.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(userMapper.usersToUserDtos(allUsers), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.userToDto(user));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(userMapper.userToDto(user));
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<List<UserDto>> getUserByRole(@PathVariable Role role) {
        List<User> users = userService.getUsersByRole(role);
        return new ResponseEntity<>(userMapper.usersToUserDtos(users), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin') or hasRole('Organizer') or hasRole('Participant')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
            userService.removeUser(id);
            return ResponseEntity.ok().build();
    }

    @PutMapping("/role/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable Long userId,
                                                  @RequestBody @Valid ChangeRoleRequest changeRoleRequest) {
        Optional<User> userOptional = userService.updateUserRole(userId, changeRoleRequest);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new ResponseEntity<>(userMapper.userToDto(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

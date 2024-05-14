package com.fmi.sporttournament.controller;

import com.fmi.sporttournament.Dto.UserDto;
import com.fmi.sporttournament.Dto.requests.ChangeRoleRequest;
import com.fmi.sporttournament.entity.User;
import com.fmi.sporttournament.entity.enums.Role;
import com.fmi.sporttournament.mapper.UserMapper;
import com.fmi.sporttournament.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<User> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(userMapper.usersToUserDtos(allUsers), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(userMapper.userToDto(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(value -> new ResponseEntity<>(userMapper.userToDto(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDto>> getUserByRole(@PathVariable Role role){
        List<User> users = userService.getUsersByRole(role);
        return new ResponseEntity<>(userMapper.usersToUserDtos(users), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.removeUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable Long userId, @RequestBody ChangeRoleRequest changeRoleRequest) {
        Optional<User> userOptional = userService.updateUserRole(userId, changeRoleRequest);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new ResponseEntity<>(userMapper.userToDto(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

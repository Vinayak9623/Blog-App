package com.vsd.controller;

import com.vsd.dto.LoginRequest;
import com.vsd.dto.TokenResponse;
import com.vsd.dto.UserDto;
import com.vsd.dto.UserResponse;
import com.vsd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public UserDto createUser(@RequestBody UserDto userDto){
        return userService.registerUser(userDto);
    }

    @PostMapping("/login")
    public TokenResponse loginUser(@RequestBody LoginRequest loginRequest){
        return userService.genrateToken(loginRequest);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/{userId}")
    public void makeAdmin(@PathVariable("userId") Long userId){
         userService.makeAdmin(userId);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }
}

package com.vsd.service;

import com.vsd.dto.LoginRequest;
import com.vsd.dto.TokenResponse;
import com.vsd.dto.UserDto;

public interface UserService {

    UserDto registerUser(UserDto userDto);

    void makeAdmin(Long userId);

    TokenResponse genrateToken(LoginRequest loginRequest);

}

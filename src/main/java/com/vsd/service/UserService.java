package com.vsd.service;

import com.vsd.dto.UserDto;

public interface UserService {

    UserDto registerUser(UserDto userDto);

    void makeAdmin(Long userId);

}

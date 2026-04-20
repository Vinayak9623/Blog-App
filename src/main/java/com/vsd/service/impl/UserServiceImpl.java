package com.vsd.service.impl;

import com.vsd.dto.UserDto;
import com.vsd.entity.Role;
import com.vsd.entity.User;
import com.vsd.repository.UserRepository;
import com.vsd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public UserDto registerUser(UserDto userDto) {
        var user=modelMapper.map(userDto,User.class);
        validate(user);
        user.setRole(Role.Role_Guest);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        var user1=userRepository.save(user);
        return modelMapper.map(user1, UserDto.class);
    }

    @Override
    public void makeAdmin(Long userId) {
        var user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        user.setRole(Role.Role_Admin);
        userRepository.save(user);
    }


    private void validate(User user){
        User user1 = userRepository.findByEmail(user.getEmail());
        if(user1!=null){
            throw new RuntimeException("User already exist");
        }
    }
}

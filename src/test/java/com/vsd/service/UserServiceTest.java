package com.vsd.service;

import com.vsd.dto.UserDto;
import com.vsd.entity.Role;
import com.vsd.entity.User;
import com.vsd.repository.UserRepository;
import com.vsd.security.JwtService;
import com.vsd.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.vsd.entity.Role.ROLE_GUEST;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  ModelMapper modelMapper;
    @Mock
    private  AuthenticationManager authenticationManager;
    @Mock
    private  JwtService jwtService;
    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void shouldRegisterUserSuccessfully(){
        UserDto userDto =new UserDto();
        User user=new User();
        user.setEmail("vinayak@gmail.com");
        userDto.setName("vinayak");
        userDto.setEmail("vinayak@gmail.com");
        userDto.setPassword("12345");
        userDto.setRole(ROLE_GUEST);
        User savedUser=new User();
        savedUser.setId(1L);
        savedUser.setName("vinayak");

        Mockito.when(modelMapper.map(userDto,User.class)).thenReturn(user);
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(userDto.getPassword())).thenReturn("EncodedPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);
        Mockito.when(modelMapper.map(savedUser,UserDto.class)).thenReturn(userDto);

//Act
        UserDto result = userService.registerUser(userDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userDto.getEmail(),result.getEmail());
        Assertions.assertEquals(userDto.getName(),result.getName());

        Mockito.verify(modelMapper).map(userDto,User.class);
        Mockito.verify(userRepository).findByEmail(user.getEmail());
        Mockito.verify(passwordEncoder).encode(userDto.getPassword());
        Mockito.verify(userRepository).save(user);
        Mockito.verify(modelMapper).map(savedUser,UserDto.class);
    }

}

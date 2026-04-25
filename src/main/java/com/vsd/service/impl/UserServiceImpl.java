package com.vsd.service.impl;

import com.vsd.dto.LoginRequest;
import com.vsd.dto.TokenResponse;
import com.vsd.dto.UserDto;
import com.vsd.entity.Role;
import com.vsd.entity.User;
import com.vsd.repository.UserRepository;
import com.vsd.security.JwtService;
import com.vsd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


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

    @Override
    public TokenResponse genrateToken(LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken
                            (loginRequest.getEmail(), loginRequest.getPassword());
            Authentication authenticate = authenticationManager
                    .authenticate(authenticationToken);

            User user = userRepository
                    .findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            TokenResponse response = TokenResponse.builder()
                    .accessToken(jwtService.genrateAccessToken(user))
                    .refreshToken(jwtService.genrateRefreshToken(user))
                    .user(modelMapper.map(user, UserDto.class))
                    .build();
            return response;
        }
        catch (AuthenticationException ex){

            ex.printStackTrace();
            throw new BadCredentialsException("Credentials not valid");
        }
    }


    private void validate(User user){
        User user1 = userRepository
                .findByEmail(user.getEmail())
                .orElseThrow(()->new RuntimeException("user not found"));
        if(user1!=null){
            throw new RuntimeException("User already exist");
        }
    }
}

package com.vsd.dto;
import com.vsd.entity.Role;

public record UserResponse(

        Long id,
        String email,
        String name,
        Role role,
        boolean isActive

) {

}

package com.example.cricket_app.mapper;

import com.example.cricket_app.dto.response.SignUpResponse;
import com.example.cricket_app.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignUpMapper {
    SignUpResponse toResponseDto(Users user);

}

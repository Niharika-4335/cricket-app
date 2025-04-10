package com.example.cricket_app.mapper;

import com.example.cricket_app.dto.request.UserRequest;
import com.example.cricket_app.dto.response.UserResponse;
import com.example.cricket_app.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toEntity(UserRequest userRequest);

    //dto to entity conversion to save into database.
    UserResponse toResponseDto(Users users);
    //entity to dto to give to client.
}

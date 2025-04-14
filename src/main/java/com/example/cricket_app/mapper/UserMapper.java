package com.example.cricket_app.mapper;

import com.example.cricket_app.dto.request.UserRequest;
import com.example.cricket_app.dto.response.UserResponse;
import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.entity.Users;
import com.example.cricket_app.entity.WalletTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toEntity(UserRequest userRequest);

    //dto to entity conversion to save into database.
    @Mapping(source = "wallet.balance", target = "balance")
    UserResponse toResponseDto(Users users);
    //to get information about one user.

    @Mapping(source = "wallet.balance", target = "balance")
    List<UserResponse> toResponseDtoList(List<Users> users);//to get information about all users.
}

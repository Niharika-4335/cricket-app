package com.example.cricket_app.mapper;

import com.example.cricket_app.dto.request.CreditWalletRequest;
import com.example.cricket_app.dto.response.WalletResponse;
import com.example.cricket_app.entity.Users;
import com.example.cricket_app.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletMapper {

//    @Mapping(target = "id",ignore = true)
//    @Mapping(target = "user", source = "user") // manually passed in service
//    @Mapping(target = "balance", source = "creditWalletRequest.amount")
//    Wallet toEntity(CreditWalletRequest creditWalletRequest,Users user);

    WalletResponse toResponseDto(Wallet wallet);
}

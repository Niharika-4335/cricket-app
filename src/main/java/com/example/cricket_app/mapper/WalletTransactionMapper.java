package com.example.cricket_app.mapper;

import com.example.cricket_app.dto.request.WalletTransactionRequest;
import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.entity.WalletTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletTransactionMapper {


    @Mapping(source = "userId", target = "wallet.user.id")

    WalletTransaction toEntity(WalletTransactionRequest walletTransactionRequest);


    @Mapping(source = "match.id", target = "matchId")
    WalletTransactionResponse toResponse(WalletTransaction walletTransaction);


    List<WalletTransactionResponse> toResponseList(List<WalletTransaction> transactions);

}

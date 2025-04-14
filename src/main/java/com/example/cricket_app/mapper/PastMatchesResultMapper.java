package com.example.cricket_app.mapper;

import com.example.cricket_app.dto.response.PastMatchesResultResponse;
import com.example.cricket_app.dto.response.WalletTransactionResponse;
import com.example.cricket_app.entity.Match;
import com.example.cricket_app.entity.WalletTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PastMatchesResultMapper {
    List<PastMatchesResultResponse> toResponseDtoList(List<Match> matchs);
}

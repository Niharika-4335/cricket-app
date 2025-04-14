package com.example.cricket_app.mapper;

import com.example.cricket_app.dto.response.BetResponse;
import com.example.cricket_app.entity.Bet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BetMapper {
    @Mapping(source = "id", target = "betId")
    @Mapping(source = "match.id", target = "matchId")
    @Mapping(source = "createdAt", target = "createdAt")
    BetResponse toResponse(Bet bet);

}

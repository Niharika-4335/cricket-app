package com.example.cricket_app.mapper;

import com.example.cricket_app.dto.request.CreateMatchRequest;
import com.example.cricket_app.dto.response.MatchResponse;
import com.example.cricket_app.entity.Match;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatchMapper {
    Match toEntity(CreateMatchRequest createMatchRequest);

    //dto to entity conversion to save into database.
    MatchResponse toResponseDto(Match match);
    //entity to dto to response..
}

package com.example.cricket_app.mapper;

import com.example.cricket_app.dto.response.PastMatchesResultResponse;
import com.example.cricket_app.entity.Match;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PastMatchesResultMapper {
    List<PastMatchesResultResponse> toResponseDtoList(List<Match> matchs);
}

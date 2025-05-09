package com.example.movie.dto;

import lombok.Data;

import java.util.List;

@Data
public class TweetResponse {
    private String keyword;
    private List<TweetsDto> results;
}

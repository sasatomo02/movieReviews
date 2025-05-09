package com.example.movie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TweetsDto {

    @JsonProperty("user")
    private String user;

    @JsonProperty("date")
    private String date;

    @JsonProperty("content")
    private String content;

    @JsonProperty("likes")
    private int likes;

    @JsonProperty("retweets")
    private int retweets;

    // getter/setter
}

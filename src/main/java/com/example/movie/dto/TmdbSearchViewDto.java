package com.example.movie.dto;

import lombok.Data;

@Data
public class TmdbSearchViewDto {
    private int id;
    private String title;
    private String releaseDate;
    private String overview;
    private double voteAverage;
    private String posterPath;
}

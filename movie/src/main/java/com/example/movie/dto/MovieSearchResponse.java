package com.example.movie.dto;
import lombok.Data;

import java.util.List;

@Data
public class MovieSearchResponse {
    private int page;
    private List<MovieDto> results;
    private int total_results;
    private int total_pages;
}


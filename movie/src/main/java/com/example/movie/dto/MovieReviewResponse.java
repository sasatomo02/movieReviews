package com.example.movie.dto;

import lombok.Data;

import java.util.List;
@Data
public class MovieReviewResponse {
    private List<MovieReview> results;
    private int total_results;
    private int total_pages;

    public int getTotalPages() {
        return this.total_pages;
    }

    // Getter and Setter
}

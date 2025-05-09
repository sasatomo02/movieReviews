package com.example.movie.dto;

import lombok.Data;

@Data
public class MovieReview {
    private String author;
    private String content;
    private String created_at;
    private String updated_at;

    // Getter and Setter
}

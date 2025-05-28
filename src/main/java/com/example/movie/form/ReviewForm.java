package com.example.movie.form;

import lombok.Data;

@Data
public class ReviewForm {
    private String username;
    private String reviews;
    private boolean isSpoiler;
    private String movieId;
}
package com.example.movie.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewsDto {
    private Long id;
    private String username;
    private String movieId;
    private String reviews;
    private LocalDateTime createdAt;
    private boolean isSpoiler;
}
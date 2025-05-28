package com.example.movie.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "reviews")
@Data
public class ReviewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String movieId;
    @Column(columnDefinition = "TEXT")
    private String reviews;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private boolean isSpoiler;
}
package com.example.movie.repository;

import com.example.movie.entity.ReviewsEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<ReviewsEntity, Long> {
    List<ReviewsEntity> findByMovieId(String movieId);
    List<ReviewsEntity> findByMovieId(String movieId, Sort sort); // ソート機能を追加

}
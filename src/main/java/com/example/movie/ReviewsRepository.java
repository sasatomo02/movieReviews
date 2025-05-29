package com.example.movie;

import com.example.movie.entity.ReviewsEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<ReviewsEntity, Long> {
    List<ReviewsEntity> findByMovieId(String movieId);
    List<ReviewsEntity> findByMovieId(String movieId, Sort sort); // ソート機能を追加

}
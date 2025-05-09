package com.example.movie.dto;

import lombok.Data;

@Data
public class TmdbReviewViewDto {
    private String author;        // 投稿者
    private String content;       // 本文
    private String createdAt;     // 投稿日（整形するかは後で）
    private Double rating;        // スコア（null許容）
    private String avatarPath;    // アバター画像（URL形式）
}

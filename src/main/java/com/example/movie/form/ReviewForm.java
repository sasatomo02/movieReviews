package com.example.movie.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewForm {
    private String username;

    @NotEmpty(message = "レビュー内容を入力してください")
    @Size(min = 1, message = "レビュー内容は1文字以上で入力してください")
    private String reviews;

    private boolean spoiler;
    private String movieId;


}
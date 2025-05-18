package com.example.movie.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MovieDetailDto {
    private String title;
    private String overview;  // あらすじ
    @JsonProperty("release_date")
    private String releaseDate;  // 公開日
    @JsonProperty("vote_average")
    private Double voteAverage;  // 評価
    @JsonProperty("poster_path")
    private String posterPath;  // ポスター画像のパス
    @JsonProperty("backdrop_path")
    private String backdropPath;  // バックグラウンド画像のパス
    private Integer runtime;  // 上映時間
    private String originalLanguage;  // 言語
}


package com.example.movie.service;

import com.example.movie.config.ApiConfig;
import com.example.movie.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TmdbApiService {

    private final RestTemplate restTemplate;
    private final ApiConfig apiConfig;


    public List<MovieDto> searchMovies(String query) {
        List<MovieDto> allResults = new ArrayList<>();
        int page = 1;

        while (true) {
            String url = "https://api.themoviedb.org/3/search/movie";
            String uri = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("api_key", apiConfig.getTmdbApiKey())
                    .queryParam("language", "ja-JP")
                    .queryParam("query", query)
                    .queryParam("page", page)  // ページ番号を指定
                    .toUriString();

            MovieSearchResponse response = restTemplate.getForObject(uri, MovieSearchResponse.class);

            if (response == null || response.getResults().isEmpty()) {
                break;
            }
            allResults.addAll(response.getResults());
            page++;
        }

        return allResults;
    }

    public List<MovieReview> getMovieReviews(int movieId) {
        String apiKey = apiConfig.getTmdbApiKey();
        int page = 1;
        List<MovieReview> allReviews = new ArrayList<>();

        // 最初のリクエストURLを構成
        String url = String.format("https://api.themoviedb.org/3/movie/%d/reviews?api_key=%s&language=ja-JP&page=%d", movieId, apiKey, page);

        // 1ページ目を取得
        ResponseEntity<MovieReviewResponse> response = restTemplate.exchange(url, HttpMethod.GET, null, MovieReviewResponse.class);
        MovieReviewResponse body = response.getBody();

        // レスポンスのログ出力
        System.out.println("Response Body: " + body);
        System.out.println("Total Pages: " + (body != null ? body.getTotalPages() : "No response"));
        System.out.println("Reviews on Page 1: " + (body != null && body.getResults() != null ? body.getResults().size() : "No reviews"));

        // レスポンスがnullでなければ、レビューを追加
        if (body != null && body.getResults() != null) {
            allReviews.addAll(body.getResults());
        }
        // 総ページ数を取得
        int totalPages = body != null ? body.getTotalPages() : 0;

        // 他のページがあれば、順番に取得して追加
        while (page < totalPages) {
            page++; // 次のページに進む
            String urlNextPage = String.format("https://api.themoviedb.org/3/movie/%d/reviews?api_key=%s&language=ja-JP&page=%d", movieId, apiKey, page);
            ResponseEntity<MovieReviewResponse> nextPageResponse = restTemplate.exchange(urlNextPage, HttpMethod.GET, null, MovieReviewResponse.class);
            MovieReviewResponse nextPageBody = nextPageResponse.getBody();

            if (nextPageBody != null && nextPageBody.getResults() != null) {
                allReviews.addAll(nextPageBody.getResults());
            }
        }
        // 全てのページのレビューが格納されたリストを返す
        return allReviews;
    }

    public MovieDetailDto getInfoById(int id){
        String apiKey = apiConfig.getTmdbApiKey();
        String url = String.format("https://api.themoviedb.org/3/movie/%d", id);
        String uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("api_key", apiConfig.getTmdbApiKey())
                .queryParam("language", "ja-JP")
                .toUriString();

        return restTemplate.getForObject(uri, MovieDetailDto.class);
    }

    public String getOfficialTrailerYoutubeId(int movieId) {
        String apiKey = apiConfig.getTmdbApiKey();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + apiKey + "&language=ja-JP";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");

        if (results != null) {
            for (Map<String, Object> video : results) {
                String site = (String) video.get("site");
                String type = (String) video.get("type");
                if ("YouTube".equalsIgnoreCase(site) && "Trailer".equalsIgnoreCase(type)) {
                    return (String) video.get("key");  // ← YouTube Video ID
                }
            }
        }

        return null;
    }

}

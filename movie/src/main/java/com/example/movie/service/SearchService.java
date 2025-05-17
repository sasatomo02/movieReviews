package com.example.movie.service;

import com.example.movie.dto.*;
import com.example.movie.form.SearchForm;
import com.github.dozermapper.core.DozerBeanMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchService {
    private final TmdbApiService tmdbApiService;
    private final GoogleCustomSearchApiService googleCustomSearchApiService;
    private final DozerBeanMapper mapper;


    public List<TmdbSearchViewDto> searchView(SearchForm form) {
        List<MovieDto> movies = tmdbApiService.searchMovies(form.getTitle());
        return movies.stream()
                .map(movie -> mapper.map(movie, TmdbSearchViewDto.class))
                .collect(Collectors.toList());
    }

    public List<TmdbReviewViewDto> getReview(int id) {
        List<MovieReview> reviews = tmdbApiService.getMovieReviews(id);
        return reviews.stream()
                .map(review -> mapper.map(review, TmdbReviewViewDto.class))
                .collect(Collectors.toList());
    }

    public MovieDetailDto getInfoById(int id) {
        var a = tmdbApiService.getInfoById(id);
        System.out.println(a.getPosterPath());
        System.out.println(a.getTitle());
        return tmdbApiService.getInfoById(id);
    }

    public List<SearchResultDto> searchGoogleNote(String title) {
        return googleCustomSearchApiService.searchNote(title,1);
    }


    public List<SearchResultDto> searchGoogleAmeba(String title) {
        var debugLog = googleCustomSearchApiService.searchAmeba(title,1);

        return debugLog;
    }

    public String youtubeTrailerUrl(int id) {
        String videoId = tmdbApiService.getOfficialTrailerYoutubeId(id);
        return (videoId != null) ? "https://www.youtube.com/embed/" + videoId : null;
    }
}

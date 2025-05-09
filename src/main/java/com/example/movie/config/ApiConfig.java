package com.example.movie.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ApiConfig {
    @Value("${tmdb.api.key}")
    String tmdbApiKey;//TMDb
    @Value("${spotify.client.id}")
    String clientId;//spotify
    @Value("${spotify.client.secret}")
    String clientSecret;//spotify
    @Value("${google.api.key}")
    private String apiKey;
    @Value("${google.api.note.cx}")
    private String noteCx;
    @Value("${youtube.api.key}")
    private String youtubeApiKey;
    @Value("${google.api.ameba.cx}")
    private String amebaCx;
}

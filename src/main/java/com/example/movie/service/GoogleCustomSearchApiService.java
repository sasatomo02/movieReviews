package com.example.movie.service;

import com.example.movie.config.ApiConfig;
import com.example.movie.dto.SearchResultDto;
import com.github.dozermapper.core.DozerBeanMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GoogleCustomSearchApiService {
    private final RestTemplate restTemplate;
    private final DozerBeanMapper mapper;
    private final ApiConfig apiConfig;

    public List<SearchResultDto> searchNote(String keyword, int startIndex) {
        String apiKey = apiConfig.getApiKey();
        String cx = apiConfig.getNoteCx();

        String query = keyword + " 感想 site:note.com";
        String url = "https://www.googleapis.com/customsearch/v1"
                + "?key=" + apiKey
                + "&cx=" + cx
                + "&q=" + query
                + "&start=" + startIndex;

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, String>> items = (List<Map<String, String>>) response.get("items");

        List<SearchResultDto> results = new ArrayList<>();
        if (items != null) {
            for (Map<String, String> item : items) {
                results.add(new SearchResultDto(item.get("title"), item.get("link")));
            }
        }
        return results;
    }


    public List<SearchResultDto> searchAmeba(String keyword, int startIndex) {
        String apiKey = apiConfig.getApiKey();
        String cx = apiConfig.getAmebaCx();

        String query = keyword + " 感想 site:ameblo.jp";
        String url = "https://www.googleapis.com/customsearch/v1"
                + "?key=" + apiKey
                + "&cx=" + cx
                + "&q=" + query
                + "&start=" + startIndex;

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, String>> items = (List<Map<String, String>>) response.get("items");

        List<SearchResultDto> results = new ArrayList<>();
        if (items != null) {
            for (Map<String, String> item : items) {
                results.add(new SearchResultDto(item.get("title"), item.get("link")));
            }
        }
        return results;
    }


}

package com.example.movie.controller;

import com.example.movie.form.SearchForm;
import com.example.movie.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/main")
    public String view(@ModelAttribute SearchForm searchForm) {
        return "view";
    }

    @PostMapping("/main")
    public String search(@ModelAttribute SearchForm searchForm, Model model) {
        var result = searchService.searchView(searchForm);
        model.addAttribute("results", result);
        return "view";
    }

    @GetMapping("/movie/{id}")
    public String detail(@PathVariable int id, Model model) {
        var reviews = searchService.getReview(id);
        var movieTitle = searchService.getInfoById(id).getTitle();
        var year = searchService.getInfoById(id).getReleaseDate();
        year = year.substring(0, 4);
        /*
        デバｯｯｯｯｯｯｯｯｯアアアアアアアアアアアアアアグ！！！！！！！！！！
         */
        boolean isDebug = false; //API制限のため、デバッグ=true、本番=false

        String movieVideo = null;
        List<?> searchNoteReviews = null;
        List<?> searchAmebaReviews = null;

        if (!isDebug) {
            searchNoteReviews = searchService.searchGoogleNote(movieTitle);
            searchAmebaReviews = searchService.searchGoogleAmeba(movieTitle);
            movieVideo = searchService.youtubeTrailerUrl(id);
        }

        model.addAttribute("youtube",movieVideo);
        model.addAttribute("reviews", reviews);
        model.addAttribute("movie", searchService.getInfoById(id));
        model.addAttribute("searchNoteResults", searchNoteReviews);
        model.addAttribute("searchAmebaResults", searchAmebaReviews);
        return "detail";
    }
}

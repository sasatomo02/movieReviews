package com.example.movie.controller;

import com.example.movie.dto.SearchResultDto;
import com.example.movie.form.SearchForm;
import com.example.movie.service.GoogleCustomSearchApiService;
import com.example.movie.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@SessionAttributes("SearchForm")
public class SearchController {
    private final SearchService searchService;
    private final GoogleCustomSearchApiService  googleCustomSearchApiService;

    @GetMapping("/main")
    public String view(@ModelAttribute SearchForm searchForm,Model model) {
        // セッションから検索フォームのデータを取得
        if (!model.containsAttribute("searchForm")) {
            model.addAttribute("searchForm", new SearchForm());
        }
        return "view"; // index.htmlを表示
    }

    @PostMapping("/main")
    public String search(@ModelAttribute SearchForm searchForm, Model model) {
        String keyword = searchForm.getTitle();
        var result = searchService.searchView(searchForm);
        model.addAttribute("results", result);
        model.addAttribute("keyword", keyword);
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
        boolean isDebug = true; //API制限のため、デバッグ=true、本番=false

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

    @GetMapping("/searchNote")
    @ResponseBody
    public List<SearchResultDto> searchNote(@RequestParam String keyword,
                                            @RequestParam(defaultValue = "1") int page) {
        int startIndex = (page - 1) * 10 + 1; // Google APIは1始まり
        return googleCustomSearchApiService.searchNote(keyword, startIndex);
    }

    @GetMapping("/searchAmeba")
    @ResponseBody
    public List<SearchResultDto> searchAmeba(@RequestParam String keyword,
                                             @RequestParam(defaultValue = "1") int page) {
        int startIndex = (page - 1) * 10 + 1;
        return googleCustomSearchApiService.searchAmeba(keyword, startIndex);
    }

}

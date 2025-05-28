package com.example.movie.controller;

import com.example.movie.entity.ReviewsEntity;
import com.example.movie.form.ReviewForm; // 追加
import com.example.movie.dto.SearchResultDto;
import com.example.movie.form.SearchForm;
import com.example.movie.repository.ReviewsRepository;
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
    private final GoogleCustomSearchApiService googleCustomSearchApiService;
    private final ReviewsRepository reviewsRepository; // final に変更

    @GetMapping("/main")
    public String view(@ModelAttribute SearchForm searchForm, Model model) {
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
    public String detail(@PathVariable int id, @RequestParam(value = "sort", required = false) String sort, Model model) {
        var reviews = searchService.getReview(id);
        var userReviews = searchService.getUserReviews(id, sort);
        var movieInfo = searchService.getInfoById(id);
        var movieTitle = movieInfo.getTitle();
        String year = movieInfo.getReleaseDate();
        if (year != null && year.length() >= 4) {
            year = year.substring(0, 4);
        }
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

        model.addAttribute("userReviews", userReviews);
        model.addAttribute("movieId", id);
        model.addAttribute("youtube", movieVideo);
        model.addAttribute("reviews", reviews);
        model.addAttribute("movie", movieInfo);
        model.addAttribute("searchNoteResults", searchNoteReviews);
        model.addAttribute("searchAmebaResults", searchAmebaReviews);
        return "detail";
    }

    @PostMapping("/reviews/{movieId}")
    public String submitReview(@PathVariable String movieId, @ModelAttribute ReviewForm reviewForm,
                               @RequestParam(value = "isSpoiler", required = false) String spoiler) {
        System.out.println("送信されたisSpoilerの値: " + spoiler);
        boolean isSpoilerValue = "true".equals(spoiler);
        reviewForm.setSpoiler(isSpoilerValue);
        System.out.println("変換後のisSpoilerの値: " + reviewForm.isSpoiler());

        ReviewsEntity newReview = new ReviewsEntity();
        newReview.setMovieId(movieId);
        newReview.setUsername(reviewForm.getUsername());
        newReview.setReviews(reviewForm.getReviews());
        newReview.setSpoiler(reviewForm.isSpoiler());

        reviewsRepository.save(newReview);

        return "redirect:/movie/" + movieId;
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
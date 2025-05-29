package com.example.movie.controller;

import com.example.movie.entity.ReviewsEntity;
import com.example.movie.form.ReviewForm;
import com.example.movie.dto.SearchResultDto;
import com.example.movie.form.SearchForm;
import com.example.movie.ReviewsRepository;
import com.example.movie.service.GoogleCustomSearchApiService;
import com.example.movie.service.SearchService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@SessionAttributes("SearchForm")
public class SearchController {
    private final SearchService searchService;
    private final GoogleCustomSearchApiService googleCustomSearchApiService;
    private final ReviewsRepository reviewsRepository;

    @GetMapping("/main")
    public String view(@ModelAttribute SearchForm searchForm, Model model) {
        if (!model.containsAttribute("searchForm")) {
            model.addAttribute("searchForm", new SearchForm());
        }
        return "view";
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
        boolean isDebug = true; //API制限のため、デバッグ=true、本番=false

        String movieVideo = null;
        List<?> searchNoteReviews = null;
        List<?> searchAmebaReviews = null;

        if (!isDebug) {
            searchNoteReviews = searchService.searchGoogleNote(movieTitle);
            searchAmebaReviews = searchService.searchGoogleAmeba(movieTitle);
            movieVideo = searchService.youtubeTrailerUrl(id);
        }

        // ★ ここで ReviewForm のインスタンスを Model に追加します ★
        model.addAttribute("reviewForm", new ReviewForm());
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
    public String submitReview(@PathVariable String movieId, @Valid @ModelAttribute ReviewForm reviewForm, BindingResult result,
                               @RequestParam(value = "isSpoiler", required = false) String spoiler, Model model) {
        boolean isSpoilerValue = "true".equals(spoiler);
        reviewForm.setSpoiler(isSpoilerValue);

        if (result.hasErrors()) {
            // ★ エラー発生時、入力された reviewForm を Model に追加 ★
            model.addAttribute("reviewForm", reviewForm);

            // ★ 既存の詳細情報を Model から取得 (もしあれば) ★
            if (!model.containsAttribute("userReviews")) {
                var userReviews = searchService.getUserReviews(Integer.parseInt(movieId), null);
                model.addAttribute("userReviews", userReviews);
            }
            if (!model.containsAttribute("movie")) {
                var movieInfo = searchService.getInfoById(Integer.parseInt(movieId));
                model.addAttribute("movie", movieInfo);
            }
            model.addAttribute("movieId", movieId);

            return "detail"; // エラーがある場合は詳細ページに戻る
        }

        ReviewsEntity newReview = new ReviewsEntity();
        newReview.setMovieId(movieId);
        // ユーザー名がない場合は「ななしの映画好き」を設定
        newReview.setUsername(reviewForm.getUsername() == null || reviewForm.getUsername().isEmpty() ? "ななしの映画好き" : reviewForm.getUsername());
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
package com.areshok.animelz.controller;

import com.areshok.animelz.model.Anime;
import com.areshok.animelz.model.AnimeRating;
import com.areshok.animelz.model.Genre;
import com.areshok.animelz.model.User;
import com.areshok.animelz.service.AnimeService;
import com.areshok.animelz.service.GenreService;
import com.areshok.animelz.service.UserService;
import com.areshok.animelz.util.ControllerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("anime")
public class AnimeController {

    private final AnimeService animeService;
    private final UserService userService;
    private final GenreService genreService;

    public AnimeController(AnimeService animeService, UserService userService, GenreService genreService) {
        this.animeService = animeService;
        this.userService = userService;
        this.genreService = genreService;
    }

    @GetMapping
    public String mainMenu(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 24) Pageable pageable, Model model) {
        Page<Anime> page = animeService.findAll(pageable);
        int[] pagination = ControllerUtils.computePagination(page);

        int minAnimeYear = 1975;
        int maxAnimeYear = 2024;
        model.addAttribute("minYear", minAnimeYear);
        model.addAttribute("maxYear", maxAnimeYear);
        model.addAttribute("pagination", pagination);
        model.addAttribute("url", "/anime");
        model.addAttribute("page", page);
        model.addAttribute("genres", genreService.findAll());
        return "allAnime";
    }

    @GetMapping("/filter")
    public String filterAnime(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 24) Pageable pageable,
            @RequestParam(value = "minYear", required = false) Integer minYear,
            @RequestParam(value = "maxYear", required = false) Integer maxYear,
            @RequestParam(value = "genre", required = false) List<String> genre,
            Model model) {

        // Set default year values if not provided
        if (minYear == null) {
            minYear = 1975;
        }
        if (maxYear == null) {
            maxYear = 2024;
        }

        Set<String> genres = (genre != null) ? new HashSet<>(genre) : new HashSet<>();

        Page<Anime> animeSearch;

        if (!genres.isEmpty()) {
            animeSearch = animeService.findByGenreAndSeasonBetween(genres, minYear, maxYear, pageable);
        } else {
            animeSearch = animeService.findBySeasonBetween(minYear, maxYear, pageable);
        }

        model.addAttribute("page", animeSearch);
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("minYear", minYear);
        model.addAttribute("maxYear", maxYear);
        model.addAttribute("url", buildUrl(minYear, maxYear, genres));

        return "allAnime";
    }

    private String buildUrl(Integer minYear, Integer maxYear, Set<String> genre) {
        StringBuilder urlBuilder = new StringBuilder("/anime/filter?");
        if (minYear != null) {
            urlBuilder.append("minYear=").append(minYear).append("&");
        }
        if (maxYear != null) {
            urlBuilder.append("maxYear=").append(maxYear).append("&");
        }
        if (!genre.isEmpty()) {
            // Convert Set<String> to a string representation for the URL
            urlBuilder.append("genre=").append(String.join("&genre=", genre)).append("&");
        }
        // Remove the trailing "&" if present
        if (urlBuilder.charAt(urlBuilder.length() - 1) == '&') {
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        return urlBuilder.toString();
    }

    @GetMapping("/{id}")
    public String getAnime(@PathVariable("id") Anime anime, Model model, Principal principal) {
        Double averageRating = animeService.getAverageRating(anime);
        List<Anime> listRecommendation = animeService.findTop6ByGenreOrderByAverageRatingDesc(anime);

        if (principal != null && principal.getName() != null) {
            User user = userService.findByUsername(principal.getName());
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("anime", anime);
            model.addAttribute("averageRating", averageRating);
            model.addAttribute("user", user);
            model.addAttribute("listRecommendation", listRecommendation);
            return "anime";
        } else {
            model.addAttribute("isAuthenticated", false);
            model.addAttribute("anime", anime);
            model.addAttribute("averageRating", averageRating);
            model.addAttribute("listRecommendation", listRecommendation);
            return "anime";
        }
    }

    @PostMapping("/processForm")
    @ResponseBody
    public ResponseEntity<String> processForm(@RequestParam("selectedOption") String selectedOption, @RequestParam("animeId") Long animeId, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Anime anime = animeService.findAnimeById(animeId);

        animeService.checkAnimeList(user, anime, selectedOption);

        userService.save(user);
        log.debug("USER changed anime option animeId={}, option={}", anime.getId(), selectedOption);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/recommendation")
    public String getRecommendedAnime(User user, Pageable pageable, Model model) {

        Page<Anime> recommendationList = animeService.getRecommendationList(user, pageable);

        model.addAttribute("listAnime", recommendationList);

        return "recommendation";
    }

    @PostMapping("/rate")
    @ResponseBody
    public ResponseEntity<String> rateAnime(@RequestParam("animeId") Long animeId, @RequestParam("rating") int rating) {
        // Get the currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        // Find the anime by its animeId
        Anime anime = animeService.findAnimeById(animeId);

        animeService.isUserAlreadyRatedAnime(user, anime, rating);

        // Create a new UserAnimeRating instance and set the user, anime, and rating
        AnimeRating userAnimeRating = animeService.createAnimeRatingInstance(user, anime, rating);
        // Save the user's rating

        user.getAnimeRatings().add(userAnimeRating);
        userService.save(user);

        log.debug("USER changed anime option animeId={}, rating={}", animeId, rating);

        // Return a success response
        return ResponseEntity.ok("Rating saved successfully");
    }

    @PostMapping("/cancelRating")
    @ResponseBody
    public ResponseEntity<String> cancelRating(@RequestParam("animeId") Long animeId) {
        // Get the currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        // Find the anime by its animeId
        Anime anime = animeService.findAnimeById(animeId);

        // Remove the user's rating for the anime
        user.getAnimeRatings().removeIf(rating -> rating.getAnime().equals(anime));
        userService.save(user);

        log.debug("User canceled rating for animeId={}", animeId);

        // Return a success response
        return ResponseEntity.ok("Rating removed successfully");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/edit/image")
    public String updateImageUrl(@RequestParam String imageUrl, @RequestParam("animeId") Long animeId) {
        animeService.updateAnimeImage(imageUrl, animeId);
        return "redirect:/user";
    }
}

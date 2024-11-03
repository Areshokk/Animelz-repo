package com.areshok.animelz.service;

import com.areshok.animelz.model.Anime;
import com.areshok.animelz.model.AnimeRating;
import com.areshok.animelz.model.Genre;
import com.areshok.animelz.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AnimeService {

    List<Anime> findAll();

    Page<Anime> findAll(Pageable pageable);

    Page<Anime> findAnimeByTitleOrAlternativeNameLikeIgnoreCase(String title, String alternativeName, Pageable pageable);

    Page<Anime> findByGenreAndSeasonBetween(Set<String> genres, int minYear, int maxYear, Pageable pageable);

    Page<Anime> getRecommendationList(User user, Pageable pageable);

    Anime findAnimeById(Long id);

    void checkAnimeList(User user, Anime anime, String selectedOption);

    void isUserAlreadyRatedAnime(User user, Anime anime, int userRating);

    AnimeRating createAnimeRatingInstance(User user, Anime anime, int rating);

    Page<Anime> findBySeasonBetween(Integer minYear, Integer maxYear, Pageable pageable);

    List<Anime> findTop6ByGenreOrderByAverageRatingDesc(Anime anime);

    void updateAnimeImage(String urlImage, Long animeId);

    Anime getRandomAnime();

    void deleteAnimeById(Long id);

    void saveInfoById(String title, String alternativeName, String type, String episodes, String status,
                      List<String> genres, String source, Integer season, String studio, String ageRestrictions,
                      String duration, String voiceover, String protagonists, String description, String videoUrl, Long id);

    Anime save(Anime anime);

    Double getAverageRating(Anime anime);
}

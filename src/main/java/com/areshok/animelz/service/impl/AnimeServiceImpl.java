package com.areshok.animelz.service.impl;

import com.areshok.animelz.model.Anime;
import com.areshok.animelz.model.AnimeRating;
import com.areshok.animelz.model.Genre;
import com.areshok.animelz.model.User;
import com.areshok.animelz.repository.AnimeRatingRepository;
import com.areshok.animelz.repository.AnimeRepository;
import com.areshok.animelz.repository.GenreRepository;
import com.areshok.animelz.repository.UserRepository;
import com.areshok.animelz.service.AnimeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AnimeServiceImpl implements AnimeService {

    private final AnimeRepository animeRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final AnimeRatingRepository animeRatingRepository;


    public AnimeServiceImpl(AnimeRepository animeRepository, UserRepository userRepository, GenreRepository genreRepository, AnimeRatingRepository animeRatingRepository) {
        this.animeRepository = animeRepository;
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.animeRatingRepository = animeRatingRepository;
    }

    @Override
    public List<Anime> findAll() {
        List<Anime> result = animeRepository.findAll();
        log.info("Completed findAll method");
        return result;
    }

    @Override
    public Page<Anime> findAll(Pageable pageable) {
        Page<Anime> result = animeRepository.findAll(pageable);
        log.info("Completed findAll Pageable method");
        return result;
    }

    @Override
    public Page<Anime> findAnimeByTitleOrAlternativeNameLikeIgnoreCase(String title, String alternativeName, Pageable pageable) {
        Page<Anime> result = animeRepository.findAnimeByTitleOrAlternativeNameLikeIgnoreCase(title, alternativeName, pageable);
        log.info("Completed findAnimeByTitleOrAlternativeNameLikeIgnoreCase method ");
        return result;
    }

    @Override
    public Page<Anime> findByGenreAndSeasonBetween(Set<String> genres, int minYear, int maxYear, Pageable pageable) {
        Page<Anime> result = animeRepository.findByGenreAndSeasonBetween(genres, minYear, maxYear, pageable);
        log.info("Completed findByGenreAndSeasonBetween method ");
        return result;
    }

    @Override
    public Page<Anime> getRecommendationList(User user, Pageable pageable) {

        // Fetch all anime in the user's lists
        Set<Anime> userAnimeList = new HashSet<>();
        userAnimeList.addAll(user.getWatchingList());
        userAnimeList.addAll(user.getWantToWatchList());
        userAnimeList.addAll(user.getWatchedList());

        // Fetch all users
        // Fetch all users
        List<User> allUsers = userRepository.findAll();

        // Filter out the current user
        allUsers.remove(user);

        // Find anime that other users have in their lists but the current user does not
        Set<Anime> recommendedAnime = new HashSet<>();
        for (User otherUser : allUsers) {
            for (Anime anime : otherUser.getWatchingList()) {
                if (!userAnimeList.contains(anime)) {
                    recommendedAnime.add(anime);
                }
            }
            for (Anime anime : otherUser.getWantToWatchList()) {
                if (!userAnimeList.contains(anime)) {
                    recommendedAnime.add(anime);
                }
            }
            for (Anime anime : otherUser.getWatchedList()) {
                if (!userAnimeList.contains(anime)) {
                    recommendedAnime.add(anime);
                }
            }
        }

        // Convert the set to a pageable list
        List<Anime> recommendedAnimeList = new ArrayList<>(recommendedAnime);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), recommendedAnimeList.size());
        Page<Anime> result = new PageImpl<>(recommendedAnimeList.subList(start, end), pageable, recommendedAnimeList.size());

        log.info("Completed getRecommendedAnime method");
        return result;
    }

    @Override
    public Anime findAnimeById(Long id) {
        Anime result = animeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Anime with id " + id + " not found"));
        log.info("Completed findAnimeById method ");
        return result;
    }

    @Override
    public void checkAnimeList(User user, Anime anime, String selectedOption) {
        if ("watching".equals(selectedOption)) {
            if (!user.getWatchingList().contains(anime)) {
                user.getWatchedList().remove(anime);
                user.getWantToWatchList().remove(anime);
                user.addToWatchingList(anime);
            }
        } else if ("wanttowatch".equals(selectedOption)) {
            if (!user.getWantToWatchList().contains(anime)) {
                user.getWatchedList().remove(anime);
                user.getWatchingList().remove(anime);
                user.addToWantToWatchList(anime);
            }
        } else if ("watched".equals(selectedOption)) {
            if (!user.getWatchedList().contains(anime)) {
                user.getWatchingList().remove(anime);
                user.getWantToWatchList().remove(anime);
                user.addToWatchedList(anime);
            }
        } else if ("unwatched".equals(selectedOption)) {
            user.getWatchingList().remove(anime);
            user.getWantToWatchList().remove(anime);
            user.getWatchedList().remove(anime);
        }
        log.info("Completed checkAnimeList method ");
    }

    @Override
    public void isUserAlreadyRatedAnime(User user, Anime anime, int userRating) {
        if (userRating == 0) {
            user.getAnimeRatings().removeIf(animeRating -> animeRating.getAnime().equals(anime));
        }

        Optional<AnimeRating> existingRating = user.getAnimeRatings().stream()
                .filter(rating -> rating.getAnime().equals(anime))
                .findFirst();

        existingRating.ifPresent(animeRating -> user.getAnimeRatings().remove(animeRating));
        log.info("Completed isUserAlreadyRatedAnime method");
    }

    @Override
    public AnimeRating createAnimeRatingInstance(User user, Anime anime, int rating) {
        AnimeRating userAnimeRating = new AnimeRating();
        userAnimeRating.setUser(user);
        userAnimeRating.setAnime(anime);
        userAnimeRating.setRating(rating);
        log.info("Completed createAnimeRatingInstance method ");
        return userAnimeRating;
    }


    @Override
    public Page<Anime> findBySeasonBetween(Integer minYear, Integer maxYear, Pageable pageable) {
        return animeRepository.findBySeasonBetween(minYear, maxYear, pageable);
    }

@Override
public List<Anime> findTop6ByGenreOrderByAverageRatingDesc(Anime anime) {
    if (anime.getGenres() == null || anime.getGenres().isEmpty()) {
        throw new IllegalArgumentException("Anime must have at least one genre.");
    }

    String animeGenre = anime.getGenres().iterator().next().getName();
    List<Anime> result = animeRepository.findTop6ByGenreOrderByAverageRatingDesc(animeGenre);
    log.info("Completed findTop6ByGenreOrderByAverageRatingDesc method");
    return result.stream()
                 .filter(a -> !a.equals(anime))
                 .collect(Collectors.toList());
}

    @Override
    public void updateAnimeImage(String urlImage, Long animeId) {
        animeRepository.updateAnimeImage(urlImage, animeId);
        log.info("Completed updateAnimeImage method ");
    }

    @Override
    public Anime getRandomAnime() {
        List<Anime> animeList = animeRepository.findAll();
        Collections.shuffle(animeList);
        Anime result = animeList.get(0);
        log.info("Completed getRandomAnime method");
        return result;
    }

    @Override
    public void deleteAnimeById(Long id) {
        animeRepository.deleteById(id);
        log.info("Completed deleteAnimeById method ");
    }

    @Override
    public void saveInfoById(String title, String alternativeName, String type, String episodes, String status,
                             List<String> genreNames, String source, Integer season, String studio, String ageRestrictions,
                             String duration, String voiceover, String protagonists, String description, String videoUrl, Long id) {

        Set<Genre> genreSet = genreNames.stream()
                .map(genreRepository::findByName)
                .collect(Collectors.toSet());

        Anime anime = animeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Anime with id " + id + " not found"));
        anime.setGenres(genreSet);

        animeRepository.saveAnimeInfoById(title, alternativeName, type, episodes, status, source, season, studio,
                ageRestrictions, duration, voiceover, protagonists, description, videoUrl, id);

        log.info("Completed saveInfoById method ");
    }

    @Override
    public Anime save(Anime anime) {
        Anime result = animeRepository.save(anime);
        log.info("Completed save method");
        return result;
    }

    @Override
    public Double getAverageRating(Anime anime) {
        List<AnimeRating> ratings = animeRepository.findRatingsByAnime(anime);

        if (ratings.isEmpty()) {
            return 0.0;
        }

        double totalRating = ratings.stream().mapToInt(AnimeRating::getRating).sum();
        double averageRating = totalRating / ratings.size();
        double rating = Math.round(averageRating);

        log.info("Completed getAverageRating method");
        return rating;
    }
}

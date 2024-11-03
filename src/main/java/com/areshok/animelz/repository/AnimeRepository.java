package com.areshok.animelz.repository;

import com.areshok.animelz.model.Anime;
import com.areshok.animelz.model.AnimeRating;
import com.areshok.animelz.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    Page<Anime> findAll(Pageable pageable);

    @Query("SELECT a FROM Anime a WHERE a.title LIKE %?1% or a.alternativeName LIKE %?2%")
    Page<Anime> findAnimeByTitleOrAlternativeNameLikeIgnoreCase(String title, String alternativeName, Pageable pageable);

    @Query("SELECT a FROM Anime a JOIN a.genres g WHERE g.name IN :genres AND a.season BETWEEN :minYear AND :maxYear")
    Page<Anime> findByGenreAndSeasonBetween(Set<String> genres, int minYear, int maxYear, Pageable pageable);

    @Query("SELECT a FROM Anime a JOIN a.genres g WHERE g.name IN :genres")
    Page<Anime> findByGenre(Set<String> genres, Pageable pageable);

    @Query("SELECT a FROM Anime a WHERE a.season BETWEEN :minYear AND :maxYear")
    Page<Anime> findBySeasonBetween(int minYear, int maxYear, Pageable pageable);

    Page<Anime> findBySeasonBetween(Integer minYear, Integer maxYear, Pageable pageable);


    @Query("SELECT a FROM Anime a JOIN a.genres g WHERE g IN :genres")
    Page<Anime> findByGenreIn(@Param("genres") Set<Genre> genres, Pageable pageable);

    @Query("SELECT a FROM Anime a JOIN a.genres g WHERE g IN :genres AND a.season BETWEEN :minYear AND :maxYear GROUP BY a HAVING COUNT(g) = :genreCount")
    Page<Anime> findByGenreAndSeasonBetween(@Param("genres") Set<Genre> genres, @Param("minYear") Integer minYear, @Param("maxYear") Integer maxYear, @Param("genreCount") long genreCount, Pageable pageable);

    @Query("SELECT a FROM Anime a JOIN a.genres g WHERE g IN :genres AND a.season BETWEEN :minYear AND :maxYear")
    Page<Anime> findByGenreInAndSeasonBetween(@Param("genres") Set<Genre> genres, @Param("minYear") Integer minYear, @Param("maxYear") Integer maxYear, Pageable pageable);

    @Query("SELECT a FROM Anime a " + "JOIN a.genres g " + "JOIN AnimeRating ar ON a.id = ar.anime.id " + "WHERE g.name = :genre " + "GROUP BY a " + "ORDER BY AVG(ar.rating) DESC")
    List<Anime> findTop6ByGenreOrderByAverageRatingDesc(@Param("genre") String genre);

    @Modifying
    @Transactional
    @Query("update Anime anime set anime.title = ?1, anime.alternativeName = ?2, anime.type = ?3," + "anime.episodes = ?4, anime.status = ?5, anime.source = ?6,  " + "anime.season = ?7, anime.studio = ?8, anime.ageRestrictions = ?9, " + "anime.duration = ?10, anime.voiceover = ?11, anime.protagonists = ?12, anime.description = ?13, anime.videoUrl = ?14 where anime.id = ?15")
    void saveAnimeInfoById(String title, String alternativeName, String type, String episodes, String status, String source, Integer season, String studio, String ageRestrictions, String duration, String voiceover, String protagonists, String description, String videoUrl, Long id);

    @Query("SELECT ar FROM AnimeRating ar WHERE ar.anime = :anime")
    List<AnimeRating> findRatingsByAnime(@Param("anime") Anime anime);

    @Modifying
    @Transactional
    @Query("update Anime anime set anime.fileName = ?1 where anime.id = ?2")
    void updateAnimeImage(String fileName, Long id);

}
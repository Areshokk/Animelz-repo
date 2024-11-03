package com.areshok.animelz.repository;

import com.areshok.animelz.model.AnimeRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AnimeRatingRepository extends JpaRepository<AnimeRating,Long> {

    List<AnimeRating> findTop6ByOrderByRatingDesc();

}

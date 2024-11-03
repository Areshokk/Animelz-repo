package com.areshok.animelz.service;

import com.areshok.animelz.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<Genre> findAll();

    Genre findByName(String name);

    Optional<Genre> findById(Long id);

    void save(String genre);

    String editGenre(Long id, String genreName);

    void deleteById(Long id);
}

package com.areshok.animelz.service.impl;

import com.areshok.animelz.model.Genre;
import com.areshok.animelz.repository.GenreRepository;
import com.areshok.animelz.service.GenreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }


    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre findByName(String name) {
        return genreRepository.findByName(name);
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }

    @Override
    public void save(String genreName) {
        Genre genre = new Genre();
        genre.setName(genreName);
        genreRepository.save(genre);

    }

    @Override
    public String editGenre(Long id, String genreName) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new RuntimeException("Genre not found"));
        genre.setName(genreName);
        genreRepository.save(genre);
        return genre.getName();
    }

    @Override
    public void deleteById(Long id) {
        genreRepository.deleteById(id);
    }
}

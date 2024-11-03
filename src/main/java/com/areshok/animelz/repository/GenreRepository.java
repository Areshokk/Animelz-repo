package com.areshok.animelz.repository;

import com.areshok.animelz.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findAll();

    Genre findByName(String name);

    Optional<Genre> findById(Long id);
}

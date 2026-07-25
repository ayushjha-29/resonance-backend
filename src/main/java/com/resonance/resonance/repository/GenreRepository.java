package com.resonance.resonance.repository;

import com.resonance.resonance.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre , Long> {
}

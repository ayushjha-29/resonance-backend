package com.resonance.resonance.repository;

import com.resonance.resonance.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}

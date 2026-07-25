package com.resonance.resonance.repository;

import com.resonance.resonance.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist , Long> {
}

package com.resonance.resonance.repository;

import com.resonance.resonance.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album , Long> {
}

package com.resonance.resonance.repository;

import com.resonance.resonance.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser , Long> {

    public Optional<AppUser> findByUsername(String username);

    public boolean existsByUsername(String username);

}

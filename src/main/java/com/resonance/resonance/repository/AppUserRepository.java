package com.resonance.resonance.repository;

import com.resonance.resonance.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser , Long> {

     Optional<AppUser> findByUsername(String username);

     boolean existsByUsername(String username);

     Optional<AppUser> findByEmail(String email);

     boolean existsByEmail(String email);

}

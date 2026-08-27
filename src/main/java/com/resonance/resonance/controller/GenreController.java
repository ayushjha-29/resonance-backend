package com.resonance.resonance.controller;

import com.resonance.resonance.dto.request.GenreRequest;
import com.resonance.resonance.dto.response.GenreResponse;
import com.resonance.resonance.dto.update.GenreUpdate;
import com.resonance.resonance.service.GenreService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
@Tag(name = "Genres" , description = "Genre management APIs")
@SecurityRequirement(name = "bearerAuth")
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<GenreResponse> addGenre(@Valid @RequestBody GenreRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.addGenre(request));

    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> getGenreById(@PathVariable Long id){

        return ResponseEntity.ok(genreService.getGenreById(id));

    }

    @GetMapping
    public ResponseEntity<List<GenreResponse>> getAllGenres(){

        return ResponseEntity.ok(genreService.getAllGenres());

    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreResponse> updateGenreById(@PathVariable Long id , @Valid @RequestBody GenreUpdate update){

        return ResponseEntity.ok(genreService.updateGenreById(id,update));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id){

        genreService.deleteGenreById(id);

        return ResponseEntity.noContent().build();

    }
}

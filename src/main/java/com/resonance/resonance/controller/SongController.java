package com.resonance.resonance.controller;

import com.resonance.resonance.dto.request.SongFilter;
import com.resonance.resonance.dto.request.SongRequest;
import com.resonance.resonance.dto.response.SongResponse;
import com.resonance.resonance.dto.update.SongUpdate;
import com.resonance.resonance.service.SongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;

    @PostMapping
    public ResponseEntity<SongResponse> addSong(@Valid @RequestBody SongRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(songService.addSong(request));

    }

    @GetMapping("/{id}")
    public ResponseEntity<SongResponse> getSongById(@PathVariable Long id){

        return ResponseEntity.ok(songService.getSongById(id));

    }

    @GetMapping
    public ResponseEntity<Page<SongResponse>> getAllSongs(SongFilter filter , @PageableDefault(size = 5 , sort = "id",direction = Sort.Direction.ASC) Pageable pageable){

        return ResponseEntity.ok(songService.getAllSongs(filter,pageable));

    }

    @PutMapping("/{id}")
    public ResponseEntity<SongResponse> updateSongById(@PathVariable Long id, @Valid @RequestBody SongUpdate update){

        return ResponseEntity.ok(songService.updateSongById(id, update));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id){

        songService.deleteSongById(id);

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/{id}/play")
    public ResponseEntity<SongResponse> playSong(@PathVariable Long id){

        return ResponseEntity.ok(songService.playSong(id));

    }

}
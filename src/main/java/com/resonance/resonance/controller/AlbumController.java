package com.resonance.resonance.controller;

import com.resonance.resonance.dto.request.AlbumRequest;
import com.resonance.resonance.dto.response.AlbumResponse;
import com.resonance.resonance.dto.update.AlbumUpdate;
import com.resonance.resonance.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping
    public ResponseEntity<AlbumResponse> addAlbum(@Valid @RequestBody AlbumRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(albumService.addAlbum(request));

    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponse> getAlbumById(@PathVariable Long id){

        return ResponseEntity.ok(albumService.getAlbumById(id));

    }

    @GetMapping
    public ResponseEntity<List<AlbumResponse>> getAllAlbums(){

        return ResponseEntity.ok(albumService.getAllAlbums());

    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponse> updateAlbumById(@PathVariable Long id, @Valid @RequestBody AlbumUpdate update){

        return ResponseEntity.ok(albumService.updateAlbumById(id, update));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id){

        albumService.deleteAlbumById(id);

        return ResponseEntity.noContent().build();

    }

}
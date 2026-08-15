package com.resonance.resonance.controller;

import com.resonance.resonance.dto.request.ArtistFilter;
import com.resonance.resonance.dto.request.ArtistRequest;
import com.resonance.resonance.dto.response.ArtistResponse;
import com.resonance.resonance.dto.update.ArtistUpdate;
import com.resonance.resonance.service.ArtistService;
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
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping
    public ResponseEntity<ArtistResponse> addArtist(@Valid @RequestBody ArtistRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.addArtist(request));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponse> getArtistById(@PathVariable Long id){

        return ResponseEntity.ok(artistService.getArtistById(id));

    }

    @GetMapping
    public ResponseEntity<Page<ArtistResponse>> getAllArtists(ArtistFilter filter , @PageableDefault(size = 5 , sort = "id",direction = Sort.Direction.ASC) Pageable pageable){

        return ResponseEntity.ok(artistService.getAllArtists(filter,pageable));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistResponse> updateArtistById(@PathVariable Long id, @Valid @RequestBody ArtistUpdate update){

        return ResponseEntity.ok(artistService.updateArtistById(id, update));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id){

        artistService.deleteArtistById(id);

        return ResponseEntity.noContent().build();

    }

}
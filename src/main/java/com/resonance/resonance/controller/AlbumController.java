package com.resonance.resonance.controller;

import com.resonance.resonance.dto.request.AlbumFilter;
import com.resonance.resonance.dto.request.AlbumRequest;
import com.resonance.resonance.dto.response.AlbumResponse;
import com.resonance.resonance.dto.update.AlbumUpdate;
import com.resonance.resonance.service.AlbumService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/albums")
@Tag(name = "Albums",description = "Album management APIs")
@SecurityRequirement(name = "bearerAuth")
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
    public ResponseEntity<Page<AlbumResponse>> getAllAlbums(AlbumFilter filter , @PageableDefault(size = 5 , sort = "id",direction = Sort.Direction.ASC) Pageable pageable){

        return ResponseEntity.ok(albumService.getAllAlbums(filter,pageable));

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
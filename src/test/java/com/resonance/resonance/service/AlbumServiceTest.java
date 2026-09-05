package com.resonance.resonance.service;

import com.resonance.resonance.dto.request.AlbumFilter;
import com.resonance.resonance.dto.request.AlbumRequest;
import com.resonance.resonance.dto.response.AlbumResponse;
import com.resonance.resonance.dto.update.AlbumUpdate;
import com.resonance.resonance.entity.Album;
import com.resonance.resonance.entity.Artist;
import com.resonance.resonance.exception.ResourceNotFoundException;
import com.resonance.resonance.mapper.AlbumMapper;
import com.resonance.resonance.repository.AlbumRepository;
import com.resonance.resonance.repository.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    AlbumRepository albumRepository;

    @Mock
    AlbumMapper albumMapper;

    @Mock
    ArtistRepository artistRepository;

    @Mock
    CacheManager cacheManager;

    @Mock
    Cache cache;

    @InjectMocks
    AlbumService albumService;


    @Test
    void addAlbum_shouldReturnAlbumResponse() {

        AlbumRequest request = new AlbumRequest();
        request.setArtistId(1L);

        Artist artist = new Artist();

        Album album = new Album();
        Album savedAlbum = new Album();

        AlbumResponse response = new AlbumResponse();

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
        when(albumMapper.toEntity(request)).thenReturn(album);
        when(albumRepository.save(album)).thenReturn(savedAlbum);
        when(albumMapper.toDTO(savedAlbum)).thenReturn(response);

        when(cacheManager.getCache(anyString())).thenReturn(cache);

        AlbumResponse result = albumService.addAlbum(request);

        assertEquals(response, result);

        verify(artistRepository).findById(1L);
        verify(albumMapper).toEntity(request);
        verify(albumRepository).save(album);
        verify(albumMapper).toDTO(savedAlbum);
    }


    @Test
    void addAlbum_shouldThrowException_whenArtistNotFound() {

        AlbumRequest request = new AlbumRequest();
        request.setArtistId(1L);

        when(artistRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> albumService.addAlbum(request)
        );

        verify(artistRepository).findById(1L);
        verify(albumRepository, never()).save(any(Album.class));
    }


    @Test
    void getAllAlbums_shouldReturnAlbums() {

        AlbumFilter filter = new AlbumFilter();

        Pageable pageable = PageRequest.of(0, 5);

        Album album1 = new Album();
        Album album2 = new Album();

        AlbumResponse response1 = new AlbumResponse();
        AlbumResponse response2 = new AlbumResponse();

        Page<Album> albumPage =
                new PageImpl<>(List.of(album1, album2), pageable, 2);

        when(albumRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(albumPage);

        when(albumMapper.toDTO(album1)).thenReturn(response1);
        when(albumMapper.toDTO(album2)).thenReturn(response2);

        Page<AlbumResponse> result =
                albumService.getAllAlbums(filter, pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(
                List.of(response1, response2),
                result.getContent()
        );

        verify(albumRepository).findAll(
                any(Specification.class),
                eq(pageable)
        );

        verify(albumMapper).toDTO(album1);
        verify(albumMapper).toDTO(album2);
    }


    @Test
    void getAlbumById_shouldReturnAlbumResponse() {

        Album album = new Album();
        AlbumResponse response = new AlbumResponse();

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(albumMapper.toDTO(album)).thenReturn(response);

        AlbumResponse result = albumService.getAlbumById(1L);

        assertEquals(response, result);

        verify(albumRepository).findById(1L);
        verify(albumMapper).toDTO(album);
    }


    @Test
    void getAlbumById_shouldThrowException_whenAlbumNotFound() {

        when(albumRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> albumService.getAlbumById(1L)
        );

        verify(albumRepository).findById(1L);
        verify(albumMapper, never()).toDTO(any(Album.class));
    }


    @Test
    void updateAlbumById_shouldReturnUpdatedAlbum() {

        Album album = new Album();

        Artist oldArtist = new Artist();

        album.setArtist(oldArtist);

        AlbumUpdate update = new AlbumUpdate();

        AlbumResponse response = new AlbumResponse();

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(albumMapper.toDTO(album)).thenReturn(response);

        AlbumResponse result = albumService.updateAlbumById(1L, update);

        assertEquals(response, result);

        verify(albumRepository).findById(1L);
        verify(albumMapper).updateAlbum(update, album);
        verify(albumMapper).toDTO(album);
    }


    @Test
    void updateAlbumById_shouldThrowException_whenAlbumNotFound() {

        AlbumUpdate update = new AlbumUpdate();

        when(albumRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> albumService.updateAlbumById(1L, update)
        );

        verify(albumRepository).findById(1L);
        verify(albumMapper, never()).updateAlbum(any(AlbumUpdate.class), any(Album.class));
    }


    @Test
    void updateAlbumById_shouldThrowException_whenTitleIsBlank() {

        Album album = new Album();

        Artist artist = new Artist();

        album.setArtist(artist);

        AlbumUpdate update = new AlbumUpdate();
        update.setTitle("   ");

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));

        assertThrows(
                IllegalArgumentException.class,
                () -> albumService.updateAlbumById(1L, update)
        );

        verify(albumMapper, never()).updateAlbum(any(AlbumUpdate.class), any(Album.class));
    }


    @Test
    void updateAlbumById_shouldThrowException_whenDescriptionIsBlank() {

        Album album = new Album();

        Artist artist = new Artist();

        album.setArtist(artist);

        AlbumUpdate update = new AlbumUpdate();
        update.setDescription("   ");

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));

        assertThrows(
                IllegalArgumentException.class,
                () -> albumService.updateAlbumById(1L, update)
        );

        verify(albumMapper, never()).updateAlbum(any(AlbumUpdate.class), any(Album.class));
    }


    @Test
    void updateAlbumById_shouldThrowException_whenArtistNotFound() {

        Album album = new Album();

        Artist oldArtist = new Artist();

        album.setArtist(oldArtist);

        AlbumUpdate update = new AlbumUpdate();
        update.setArtistId(2L);

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(artistRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> albumService.updateAlbumById(1L, update)
        );

        verify(artistRepository).findById(2L);
        verify(albumMapper, never()).updateAlbum(any(AlbumUpdate.class), any(Album.class));
    }


    @Test
    void deleteAlbumById_shouldDeleteAlbum() {

        Album album = new Album();

        Artist artist = new Artist();

        album.setArtist(artist);

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));

        when(cacheManager.getCache(anyString())).thenReturn(cache);

        albumService.deleteAlbumById(1L);

        verify(albumRepository).findById(1L);
        verify(albumRepository).delete(album);
    }


    @Test
    void deleteAlbumById_shouldThrowException_whenAlbumNotFound() {

        when(albumRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> albumService.deleteAlbumById(1L)
        );

        verify(albumRepository).findById(1L);
        verify(albumRepository, never()).delete(any(Album.class));
    }
}
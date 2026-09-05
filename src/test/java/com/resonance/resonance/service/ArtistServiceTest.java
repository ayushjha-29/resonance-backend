package com.resonance.resonance.service;

import com.resonance.resonance.dto.request.ArtistFilter;
import com.resonance.resonance.dto.request.ArtistRequest;
import com.resonance.resonance.dto.response.ArtistResponse;
import com.resonance.resonance.dto.update.ArtistUpdate;
import com.resonance.resonance.entity.Artist;
import com.resonance.resonance.exception.ResourceNotFoundException;
import com.resonance.resonance.mapper.ArtistMapper;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Mock
    ArtistRepository artistRepository;

    @Mock
    ArtistMapper artistMapper;

    @Mock
    CacheManager cacheManager;

    @Mock
    Cache cache;

    @InjectMocks
    ArtistService artistService;


    @Test
    void addArtist_shouldReturnArtistResponse() {

        ArtistRequest request = new ArtistRequest();

        Artist artist = new Artist();
        Artist savedArtist = new Artist();

        ArtistResponse response = new ArtistResponse();

        when(artistMapper.toEntity(request)).thenReturn(artist);
        when(artistRepository.save(artist)).thenReturn(savedArtist);
        when(artistMapper.toDTO(savedArtist)).thenReturn(response);

        ArtistResponse result = artistService.addArtist(request);

        assertEquals(response, result);

        verify(artistMapper).toEntity(request);
        verify(artistRepository).save(artist);
        verify(artistMapper).toDTO(savedArtist);

    }


    @Test
    void getAllArtists_shouldReturnArtists() {

        ArtistFilter filter = new ArtistFilter();

        Pageable pageable = PageRequest.of(0, 5);

        Artist artist1 = new Artist();
        Artist artist2 = new Artist();

        ArtistResponse response1 = new ArtistResponse();
        ArtistResponse response2 = new ArtistResponse();

        Page<Artist> artistPage = new PageImpl<>(List.of(artist1, artist2), pageable, 2);

        when(artistRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(artistPage);
        when(artistMapper.toDTO(artist1)).thenReturn(response1);
        when(artistMapper.toDTO(artist2)).thenReturn(response2);

        Page<ArtistResponse> result = artistService.getAllArtists(filter, pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(List.of(response1, response2), result.getContent());

        verify(artistRepository).findAll(any(Specification.class), eq(pageable));
        verify(artistMapper).toDTO(artist1);
        verify(artistMapper).toDTO(artist2);

    }


    @Test
    void getArtistById_shouldReturnArtistResponse() {

        Artist artist = new Artist();

        ArtistResponse response = new ArtistResponse();

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
        when(artistMapper.toDTO(artist)).thenReturn(response);

        ArtistResponse result = artistService.getArtistById(1L);

        assertEquals(response, result);

        verify(artistRepository).findById(1L);
        verify(artistMapper).toDTO(artist);

    }


    @Test
    void getArtistById_shouldThrowException_whenArtistNotFound() {

        when(artistRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> artistService.getArtistById(1L));

        verify(artistRepository).findById(1L);
        verify(artistMapper, never()).toDTO(any(Artist.class));

    }


    @Test
    void updateArtistById_shouldReturnUpdatedArtist() {

        Artist artist = new Artist();

        ArtistUpdate update = new ArtistUpdate();

        ArtistResponse response = new ArtistResponse();

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
        when(artistMapper.toDTO(artist)).thenReturn(response);

        ArtistResponse result = artistService.updateArtistById(1L, update);

        assertEquals(response, result);

        verify(artistRepository).findById(1L);
        verify(artistMapper).updateArtist(update, artist);
        verify(artistMapper).toDTO(artist);

    }


    @Test
    void updateArtistById_shouldThrowException_whenArtistNotFound() {

        ArtistUpdate update = new ArtistUpdate();

        when(artistRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> artistService.updateArtistById(1L, update));

        verify(artistRepository).findById(1L);
        verify(artistMapper, never()).updateArtist(any(ArtistUpdate.class), any(Artist.class));

    }


    @Test
    void updateArtistById_shouldThrowException_whenNameIsBlank() {

        Artist artist = new Artist();

        ArtistUpdate update = new ArtistUpdate();
        update.setName("   ");

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        assertThrows(IllegalArgumentException.class, () -> artistService.updateArtistById(1L, update));

        verify(artistMapper, never()).updateArtist(any(ArtistUpdate.class), any(Artist.class));

    }


    @Test
    void updateArtistById_shouldThrowException_whenBioIsBlank() {

        Artist artist = new Artist();

        ArtistUpdate update = new ArtistUpdate();
        update.setBio("   ");

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        assertThrows(IllegalArgumentException.class, () -> artistService.updateArtistById(1L, update));

        verify(artistMapper, never()).updateArtist(any(ArtistUpdate.class), any(Artist.class));

    }


    @Test
    void updateArtistById_shouldThrowException_whenCountryIsBlank() {

        Artist artist = new Artist();

        ArtistUpdate update = new ArtistUpdate();
        update.setCountry("   ");

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        assertThrows(IllegalArgumentException.class, () -> artistService.updateArtistById(1L, update));

        verify(artistMapper, never()).updateArtist(any(ArtistUpdate.class), any(Artist.class));

    }


    @Test
    void deleteArtistById_shouldDeleteArtist() {

        Artist artist = new Artist();

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        artistService.deleteArtistById(1L);

        verify(artistRepository).findById(1L);
        verify(artistRepository).delete(artist);

    }


    @Test
    void deleteArtistById_shouldThrowException_whenArtistNotFound() {

        when(artistRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> artistService.deleteArtistById(1L));

        verify(artistRepository).findById(1L);
        verify(artistRepository, never()).delete(any(Artist.class));

    }

}
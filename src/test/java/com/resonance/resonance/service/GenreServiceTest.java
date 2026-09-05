package com.resonance.resonance.service;

import com.resonance.resonance.dto.request.GenreRequest;
import com.resonance.resonance.dto.response.GenreResponse;
import com.resonance.resonance.dto.update.GenreUpdate;
import com.resonance.resonance.entity.Genre;
import com.resonance.resonance.exception.ResourceNotFoundException;
import com.resonance.resonance.mapper.GenreMapper;
import com.resonance.resonance.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private GenreService genreService;


    @Test
    void addGenre_shouldAddGenre(){

        GenreRequest request = new GenreRequest();

        Genre genre = new Genre();

        GenreResponse response = new GenreResponse();

        when(genreMapper.toEntity(request)).thenReturn(genre);
        when(genreMapper.toDTO(genre)).thenReturn(response);

        when(genreRepository.save(genre)).thenReturn(genre);

        GenreResponse result = genreService.addGenre(request);

        assertEquals(response , result);

        verify(genreRepository).save(genre);

        verify(genreMapper).toEntity(request);
        verify(genreMapper).toDTO(genre);

    }

    @Test
    void getAllGenres_shouldReturnGenres(){

        Genre genre1 = new Genre();

        Genre genre2 = new Genre();

        when(genreRepository.findAll()).thenReturn(List.of(genre1,genre2));

        GenreResponse response1 = new GenreResponse();
        GenreResponse response2 = new GenreResponse();

        when(genreMapper.toDTOs(List.of(genre1,genre2))).thenReturn(List.of(response1,response2));

        List<GenreResponse> result = genreService.getAllGenres();

        assertEquals(List.of(response1,response2) , result);

        verify(genreRepository).findAll();

        verify(genreMapper).toDTOs(List.of(genre1,genre2));

    }

    @Test
    void getGenreById_shouldReturnGenre(){

        Genre genre = new Genre();

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        GenreResponse response = new GenreResponse();

        when(genreMapper.toDTO(genre)).thenReturn(response);

        GenreResponse result = genreService.getGenreById(1L);

        assertEquals(response,result);

        verify(genreRepository).findById(1L);

        verify(genreMapper).toDTO(genre);

    }

    @Test
    void getGenreById_shouldThrowException_whenGenreNotFound(){

        when(genreRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,() -> genreService.getGenreById(999L));

        verify(genreRepository).findById(999L);

        verify(genreMapper ,never()).toDTO(any());

    }

    @Test
    void updateGenreById_shouldUpdateGenre(){

        GenreUpdate update = new GenreUpdate();

        Genre genre = new Genre();

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        GenreResponse response = new GenreResponse();

        when(genreMapper.toDTO(genre)).thenReturn(response);

        GenreResponse result = genreService.updateGenreById(1L,update);

        assertEquals(response,result);

        verify(genreRepository).findById(1L);

        verify(genreMapper).toDTO(genre);
        verify(genreMapper).updateGenre(update,genre);

    }

    @Test
    void updateGenreById_shouldThrowException_whenGenreNotFound(){

        GenreUpdate update = new GenreUpdate();

        Genre genre = new Genre();

        when(genreRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class , () -> genreService.updateGenreById(999L,update));

        verify(genreRepository).findById(999L);

        verify(genreMapper , never()).updateGenre(update,genre);
        verify(genreMapper , never()).toDTO(genre);

    }

    @Test
    void updateGenreById_shouldThrowException_whenNameIsEmpty(){

        GenreUpdate update = new GenreUpdate();

        Genre genre = new Genre();

        update.setName(" ");

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        assertThrows(IllegalArgumentException.class , () -> genreService.updateGenreById(1L,update));

        verify(genreRepository).findById(1L);

        verify(genreMapper , never()).updateGenre(update,genre);
        verify(genreMapper , never()).toDTO(genre);

    }

    @Test
    void updateGenreById_shouldThrowException_whenDescriptionIsEmpty(){

        GenreUpdate update = new GenreUpdate();

        Genre genre = new Genre();

        update.setDescription(" ");

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        assertThrows(IllegalArgumentException.class , () -> genreService.updateGenreById(1L,update));

        verify(genreRepository).findById(1L);

        verify(genreMapper , never()).updateGenre(update,genre);
        verify(genreMapper , never()).toDTO(genre);

    }

    @Test
    void deleteGenreById_shouldDeleteGenre(){

        Genre genre = new Genre();

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        genreService.deleteGenreById(1L);

        verify(genreRepository).delete(genre);

    }

    @Test
    void deleteGenreById_shouldThrowException_whenGenreNotFound(){

        when(genreRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class , () -> genreService.deleteGenreById(999L));

        verify(genreRepository).findById(999L);
        verify(genreRepository , never()).delete(any());

    }

}

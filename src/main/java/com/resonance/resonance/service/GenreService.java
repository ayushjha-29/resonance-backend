package com.resonance.resonance.service;

import com.resonance.resonance.dto.request.GenreRequest;
import com.resonance.resonance.dto.response.GenreResponse;
import com.resonance.resonance.dto.update.GenreUpdate;
import com.resonance.resonance.entity.Genre;
import com.resonance.resonance.entity.Song;
import com.resonance.resonance.exception.ResourceNotFoundException;
import com.resonance.resonance.mapper.GenreMapper;
import com.resonance.resonance.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    private final CacheManager cacheManager;

    private void evictCache(String cacheName , Long id){

        cacheManager.getCache(cacheName).evict(id);

    }

    private Genre getGenre(Long id){

        return genreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Genre with id "+id+" not found."));

    }

    @CacheEvict(value = "allGenres",allEntries = true)
    public GenreResponse addGenre(GenreRequest request){

        Genre genre = genreMapper.toEntity(request);

        Genre savedGenre = genreRepository.save(genre);

        return genreMapper.toDTO(savedGenre);

    }

    @Cacheable(value = "allGenres")
    public List<GenreResponse> getAllGenres(){

        List<Genre> genres = genreRepository.findAll();

        return genreMapper.toDTOs(genres);

    }

    @Cacheable(value = "genres",key = "#id")
    public GenreResponse getGenreById(Long id){

        Genre genre = getGenre(id);

        return genreMapper.toDTO(genre);

    }

    @Caching(

            put = @CachePut(value = "genres",key = "#id"),
            evict = @CacheEvict(value = "allGenres" , allEntries = true)

    )
    public GenreResponse updateGenreById(Long id ,GenreUpdate update){

        Genre genre = getGenre(id);

        if(update.getName() != null && update.getName().isBlank()){
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        if(update.getDescription() != null && update.getDescription().isBlank()){
            throw new IllegalArgumentException("Description cannot be empty.");
        }

        genreMapper.updateGenre(update , genre);

        for(Song song : genre.getSongs()){

            evictCache("songs",song.getId());

        }

        return genreMapper.toDTO(genre);

    }

    @Caching(

            evict = {

                    @CacheEvict(value = "genres",key = "#id"),
                    @CacheEvict(value = "allGenres",allEntries = true)

            }

    )
    public void deleteGenreById(Long id){

        Genre genre = getGenre(id);

        for(Song song : genre.getSongs()){

            evictCache("songs",song.getId());

        }

        genreRepository.delete(genre);

    }

}

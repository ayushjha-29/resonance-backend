package com.resonance.resonance.service;

import com.resonance.resonance.dto.request.GenreRequest;
import com.resonance.resonance.dto.response.GenreResponse;
import com.resonance.resonance.dto.update.GenreUpdate;
import com.resonance.resonance.entity.Genre;
import com.resonance.resonance.mapper.GenreMapper;
import com.resonance.resonance.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    private Genre getGenre(Long id){

        return genreRepository.findById(id).orElseThrow();

    }

    public GenreResponse addGenre(GenreRequest request){

        Genre genre = genreMapper.toEntity(request);

        Genre savedGenre = genreRepository.save(genre);

        return genreMapper.toDTO(savedGenre);

    }

    public List<GenreResponse> getAllGenres(){

        List<Genre> genres = genreRepository.findAll();

        return genreMapper.toDTOs(genres);

    }

    public GenreResponse getGenreById(Long id){

        Genre genre = getGenre(id);

        return genreMapper.toDTO(genre);

    }

    public GenreResponse updateGenreById(Long id ,GenreUpdate update){

        Genre genre = getGenre(id);

        if(update.getName() != null && update.getName().isBlank()){
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        if(update.getDescription() != null && update.getDescription().isBlank()){
            throw new IllegalArgumentException("Description cannot be empty.");
        }

        genreMapper.updateGenre(update , genre);

        return genreMapper.toDTO(genre);

    }

    public void deleteGenreById(Long id){

        Genre genre = getGenre(id);

        genreRepository.delete(genre);

    }

}

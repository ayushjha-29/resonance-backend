package com.resonance.resonance.service;

import com.resonance.resonance.dto.request.ArtistRequest;
import com.resonance.resonance.dto.response.ArtistResponse;
import com.resonance.resonance.dto.update.ArtistUpdate;
import com.resonance.resonance.entity.Artist;
import com.resonance.resonance.mapper.ArtistMapper;
import com.resonance.resonance.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    private Artist getArtist(Long id){

        return artistRepository.findById(id).orElseThrow();

    }


    public ArtistResponse addArtist(ArtistRequest request){

        Artist artist = artistMapper.toEntity(request);

        Artist savedArtist = artistRepository.save(artist);

        return artistMapper.toDTO(savedArtist);

    }

    public List<ArtistResponse> getAllArtists(){

        List<Artist> artists = artistRepository.findAll();

        return artistMapper.toDTOs(artists);

    }

    public ArtistResponse getArtistById(Long id){

        Artist artist = getArtist(id);

        return artistMapper.toDTO(artist);

    }

    public ArtistResponse updateArtistById(Long id , ArtistUpdate update){

        Artist artist = getArtist(id);

        if(update.getName() != null && update.getName().isBlank()){
            throw new IllegalArgumentException("Name cannot be blanked.");
        }

        if(update.getBio() != null && update.getBio().isBlank()){
            throw new IllegalArgumentException("Bio cannot be blanked.");
        }

        if(update.getCountry() != null && update.getCountry().isBlank()){
            throw new IllegalArgumentException("Country cannot be blanked.");
        }

        artistMapper.updateArtist(update , artist);

        return artistMapper.toDTO(artist);

    }


    public void deleteArtistById(Long id){

        Artist artist = getArtist(id);

        artistRepository.delete(artist);

    }

}

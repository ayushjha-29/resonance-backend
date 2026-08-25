package com.resonance.resonance.service;

import com.resonance.resonance.dto.request.ArtistFilter;
import com.resonance.resonance.dto.request.ArtistRequest;
import com.resonance.resonance.dto.response.ArtistResponse;
import com.resonance.resonance.dto.update.ArtistUpdate;
import com.resonance.resonance.entity.Album;
import com.resonance.resonance.entity.Artist;
import com.resonance.resonance.entity.Song;
import com.resonance.resonance.exception.ResourceNotFoundException;
import com.resonance.resonance.mapper.ArtistMapper;
import com.resonance.resonance.repository.ArtistRepository;
import com.resonance.resonance.specification.ArtistSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;
    private final CacheManager cacheManager;

    private void evictCache(String cacheName , Long id){

        cacheManager.getCache(cacheName).evict(id);

    }

    private Artist getArtist(Long id){

        return artistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Artist with id "+id+" not found."));

    }


    public ArtistResponse addArtist(ArtistRequest request){

        Artist artist = artistMapper.toEntity(request);

        Artist savedArtist = artistRepository.save(artist);

        return artistMapper.toDTO(savedArtist);

    }

    public Page<ArtistResponse> getAllArtists(ArtistFilter filter , Pageable pageable){

        Specification<Artist> specification = ArtistSpecification.buildSpecification(filter);

        Page<Artist> artists = artistRepository.findAll(specification,pageable);

        return artists.map(artist -> artistMapper.toDTO(artist));

    }

    @Cacheable(value = "artists", key = "#id")
    public ArtistResponse getArtistById(Long id){

        Artist artist = getArtist(id);

        return artistMapper.toDTO(artist);

    }

    @CachePut(value = "artists",key = "#id")
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

        for(Album album : artist.getAlbums()){

            evictCache("albums",album.getId());

        }

        for(Song song : artist.getSongs()){

            evictCache("songs",song.getId());

        }

        return artistMapper.toDTO(artist);

    }

    @CacheEvict(value = "artists",key = "#id")
    public void deleteArtistById(Long id){

        Artist artist = getArtist(id);

        for(Album album : artist.getAlbums()){

            evictCache("albums",album.getId());

        }

        for(Song song : artist.getSongs()){

            evictCache("songs",song.getId());

        }

        artistRepository.delete(artist);

    }

}

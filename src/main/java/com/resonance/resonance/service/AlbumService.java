package com.resonance.resonance.service;

import com.resonance.resonance.dto.request.AlbumFilter;
import com.resonance.resonance.dto.request.AlbumRequest;
import com.resonance.resonance.dto.response.AlbumResponse;
import com.resonance.resonance.dto.update.AlbumUpdate;
import com.resonance.resonance.entity.Album;
import com.resonance.resonance.entity.Artist;
import com.resonance.resonance.entity.Song;
import com.resonance.resonance.exception.ResourceNotFoundException;
import com.resonance.resonance.mapper.AlbumMapper;
import com.resonance.resonance.repository.AlbumRepository;
import com.resonance.resonance.repository.ArtistRepository;
import com.resonance.resonance.specification.AlbumSpecification;
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
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final ArtistRepository artistRepository;
    private final CacheManager cacheManager;

    private void evictCache(String cacheName , Long id){

        cacheManager.getCache(cacheName).evict(id);

    }

    private Album getAlbum(Long id){

        return albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Album with id "+id+" not found."));

    }

    private Artist getArtist(Long id){

        return artistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Artist with id "+id+" not found."));

    }

    public AlbumResponse addAlbum(AlbumRequest request){

        Artist artist = getArtist(request.getArtistId());

        Album album = albumMapper.toEntity(request);

        album.setArtist(artist);

        Album savedAlbum = albumRepository.save(album);

        evictCache("artists",album.getArtist().getId());

        return albumMapper.toDTO(savedAlbum);

    }

    public Page<AlbumResponse> getAllAlbums(AlbumFilter filter, Pageable pageable){

        Specification<Album> specification = AlbumSpecification.buildSpecification(filter);

        Page<Album> albums = albumRepository.findAll(specification,pageable);

        return albums.map(album -> albumMapper.toDTO(album));

    }

    @Cacheable(value = "albums", key = "#id")
    public AlbumResponse getAlbumById(Long id){

        Album album = getAlbum(id);

        return albumMapper.toDTO(album);

    }

    @CachePut(value = "albums",key = "#id")
    public AlbumResponse updateAlbumById(Long id , AlbumUpdate update){

        Album album = getAlbum(id);

        Long oldArtistId = album.getArtist().getId();
        Long newArtistId = update.getArtistId();


        if(update.getTitle() !=null && update.getTitle().isBlank()){
            throw new IllegalArgumentException("Title cannot be blank.");
        }

        if(update.getDescription() !=null && update.getDescription().isBlank()){
            throw new IllegalArgumentException("Description cannot be blank.");
        }

        if(update.getArtistId() != null) {
            Artist artist = getArtist(update.getArtistId());
            album.setArtist(artist);
        }

        albumMapper.updateAlbum(update , album);

        if(update.getTitle() != null){

            for(Song song : album.getSongs()){

                evictCache("songs",song.getId());

            }

            evictCache("artists",album.getArtist().getId());

        }

        if(newArtistId != null){

            evictCache("artists",oldArtistId);
            evictCache("artists",newArtistId);

        }

        return albumMapper.toDTO(album);

    }

    @CacheEvict(value = "albums",key = "#id")
    public void deleteAlbumById(Long id){

        Album album = getAlbum(id);

        for(Song song : album.getSongs()){

            evictCache("songs",song.getId());

        }

        evictCache("artists",album.getArtist().getId());

        albumRepository.delete(album);

    }

}

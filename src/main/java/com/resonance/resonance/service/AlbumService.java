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
import com.resonance.resonance.specification.AlbumSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final ArtistRepository artistRepository;

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

        return albumMapper.toDTO(savedAlbum);

    }

    public Page<AlbumResponse> getAllAlbums(AlbumFilter filter, Pageable pageable){

        Specification<Album> specification = AlbumSpecification.buildSpecification(filter);

        Page<Album> albums = albumRepository.findAll(specification,pageable);

        return albums.map(album -> albumMapper.toDTO(album));

    }

    public AlbumResponse getAlbumById(Long id){

        Album album = getAlbum(id);

        return albumMapper.toDTO(album);

    }

    public AlbumResponse updateAlbumById(Long id , AlbumUpdate update){

        Album album = getAlbum(id);


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

        return albumMapper.toDTO(album);

    }

    public void deleteAlbumById(Long id){

        Album album = getAlbum(id);

        albumRepository.delete(album);

    }

}

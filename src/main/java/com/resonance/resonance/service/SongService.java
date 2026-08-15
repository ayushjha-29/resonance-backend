package com.resonance.resonance.service;

import com.resonance.resonance.dto.request.SongFilter;
import com.resonance.resonance.dto.request.SongRequest;
import com.resonance.resonance.dto.response.SongResponse;
import com.resonance.resonance.dto.update.SongUpdate;
import com.resonance.resonance.entity.Album;
import com.resonance.resonance.entity.Artist;
import com.resonance.resonance.entity.Genre;
import com.resonance.resonance.entity.Song;
import com.resonance.resonance.exception.ResourceNotFoundException;
import com.resonance.resonance.mapper.SongMapper;
import com.resonance.resonance.repository.AlbumRepository;
import com.resonance.resonance.repository.ArtistRepository;
import com.resonance.resonance.repository.GenreRepository;
import com.resonance.resonance.repository.SongRepository;
import com.resonance.resonance.specification.SongSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;
    private final SongMapper songMapper;


    private Song getSong(Long id){

        return songRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Song with id "+id+" not found."));

    }

    private Artist getArtist(Long id){

        return artistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Artist with id "+id+" not found."));

    }

    private Album getAlbum(Long id){

        return albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Album with id "+id+" not found."));

    }

    private Genre getGenre(Long id){

        return genreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Genre with id "+id+" not found."));

    }


    public SongResponse addSong(SongRequest request){

        Song song = songMapper.toEntity(request);

        Genre genre = getGenre(request.getGenreId());

        song.setGenre(genre);

        Album album = getAlbum(request.getAlbumId());

        song.setAlbum(album);

        List<Artist> artists = new ArrayList<>();

        for(Long artistId : request.getArtistIds()){

            artists.add(getArtist(artistId));

        }

        song.setArtists(artists);

        for(Artist artist : artists){

            artist.getSongs().add(song);

        }

        song.setPlayCount(0L);

        Song savedSong = songRepository.save(song);

        return songMapper.toDTO(savedSong);

    }

    public Page<SongResponse> getAllSongs(SongFilter filter , Pageable pageable){

        Specification<Song> specification = SongSpecification.buildSpecification(filter);

        Page<Song> songs = songRepository.findAll(specification,pageable);

        return songs.map(song -> songMapper.toDTO(song));

    }

    public SongResponse getSongById(Long id){

        Song song = getSong(id);

        return songMapper.toDTO(song);

    }

    public SongResponse updateSongById(Long id , SongUpdate update){

        Song song = getSong(id);

        if(update.getTitle() != null && update.getTitle().isBlank()){

            throw new IllegalArgumentException("Title cannot be blank.");

        }

        if(update.getAlbumId() != null){

            Album album = getAlbum(update.getAlbumId());
            song.setAlbum(album);

        }

        if(update.getGenreId() != null){

            Genre genre = getGenre(update.getGenreId());
            song.setGenre(genre);

        }

        if(update.getArtistIds() != null){

            List<Artist> artists = new ArrayList<>();

            for(Long updateId : update.getArtistIds()){

                artists.add(getArtist(updateId));

            }

            song.setArtists(artists);

            for(Artist artist : artists){

                artist.getSongs().add(song);

            }

        }

        songMapper.updateSong(update , song);

        return songMapper.toDTO(song);

    }

    public void deleteSongById(Long id){

        Song song = getSong(id);

        songRepository.delete(song);

    }

    public SongResponse playSong(Long id){

        Song song = getSong(id);

        song.setPlayCount(song.getPlayCount()+1);

        Song savedSong = songRepository.save(song);

        return songMapper.toDTO(savedSong);

    }

}

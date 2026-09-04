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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private SongMapper songMapper;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private SongService songService;

    @Test
    void getSongById_shouldReturnSong(){

        Song song = new Song();
        SongResponse songResponse = new SongResponse();


        when(songRepository.findById(5L)).thenReturn(Optional.of(song));
        when(songMapper.toDTO(song)).thenReturn(songResponse);

        SongResponse result = songService.getSongById(5L);

        assertEquals(songResponse,result);

        verify(songRepository,times(1)).findById(5L);
        verify(songMapper,times(1)).toDTO(song);

    }

    @Test
    void getSongById_shouldThrowException_whenSongDoesNotExist(){

        when(songRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()->songService.getSongById(999L));

        verify(songRepository,times(1)).findById(999L);
        verify(songMapper , never()).toDTO(any());

    }

    @Test
    void getAllSongs_shouldReturnSongs(){

        SongFilter filter = new SongFilter();

        Pageable pageable = PageRequest.of(0,5);

        Song song1 = new Song();
        Song song2 = new Song();

        SongResponse response1 = new SongResponse();
        SongResponse response2 = new SongResponse();

        Page<Song> songPage = new PageImpl<>(List.of(song1,song2) , pageable , 2);

        when(songRepository.findAll(any(Specification.class) , eq(pageable))).thenReturn(songPage);

        when(songMapper.toDTO(song1)).thenReturn(response1);
        when(songMapper.toDTO(song2)).thenReturn(response2);

        Page<SongResponse> result = songService.getAllSongs(filter,pageable);

        assertEquals(2 , result.getTotalElements());
        assertEquals(List.of(response1 , response2) , result.getContent());

        verify(songRepository).findAll(any(Specification.class) , eq(pageable));

        verify(songMapper).toDTO(song1);
        verify(songMapper).toDTO(song2);

    }

    @Test
    void addSong_shouldSaveSong(){

        SongRequest request = new SongRequest();

        request.setGenreId(1L);
        request.setAlbumId(2L);
        request.setArtistIds(List.of(3L,4L));

        Song song = new Song();
        Genre genre = new Genre();
        Album album = new Album();
        Artist artist1 = new Artist();
        Artist artist2 = new Artist();

        artist1.setSongs(new ArrayList<>());
        artist2.setSongs(new ArrayList<>());

        SongResponse response = new SongResponse();

        when(songMapper.toEntity(request)).thenReturn(song);

        when(songRepository.save(song)).thenReturn(song);

        when(songMapper.toDTO(song)).thenReturn(response);

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        when(albumRepository.findById(2L)).thenReturn(Optional.of(album));

        when(artistRepository.findById(3L)).thenReturn(Optional.of(artist1));

        when(artistRepository.findById(4L)).thenReturn(Optional.of(artist2));

        when(cacheManager.getCache("genres")).thenReturn(cache);

        when(cacheManager.getCache("albums")).thenReturn(cache);

        when(cacheManager.getCache("artists")).thenReturn(cache);

        SongResponse result = songService.addSong(request);

        assertEquals(response,result);

        assertEquals(0L,song.getPlayCount());

        assertEquals(genre,song.getGenre());

        assertEquals(album,song.getAlbum());

        assertEquals(List.of(artist1,artist2),song.getArtists());

        assertTrue(artist1.getSongs().contains(song));

        assertTrue(artist2.getSongs().contains(song));

        verify(songMapper).toEntity(request);

        verify(genreRepository).findById(1L);

        verify(albumRepository).findById(2L);

        verify(artistRepository).findById(3L);

        verify(artistRepository).findById(4L);

        verify(songRepository).save(song);

        verify(songMapper).toDTO(song);

        verify(cacheManager).getCache("genres");

        verify(cacheManager).getCache("albums");

        verify(cacheManager , times(2)).getCache("artists");

        verify(cache).evict(1L);

        verify(cache).evict(2L);

        verify(cache).evict(3L);

        verify(cache).evict(4L);
    }

    @Test
    void addSong_shouldThrowException_whenGenreDoesNotExist(){

        SongRequest request = new SongRequest();

        request.setGenreId(999L);

        when(genreRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class , () -> songService.addSong(request));

        verify(genreRepository).findById(999L);

        verify(albumRepository , never()).findById(any());

        verify(artistRepository , never()).findById(any());

        verify(songRepository , never()).save(any());

    }

    @Test
    void addSong_shouldThrowException_whenAlbumDoesNotExist(){

        SongRequest request = new SongRequest();

        Genre genre = new Genre();

        Song song = new Song();

        request.setGenreId(1L);

        request.setAlbumId(999L);

        when(songMapper.toEntity(request)).thenReturn(song);

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        when(albumRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class , () -> songService.addSong(request));

        verify(genreRepository).findById(1L);

        verify(albumRepository).findById(999L);

        verify(artistRepository , never()).findById(any());

        verify(songRepository , never()).save(any());

    }

    @Test
    void addSong_shouldThrowException_whenArtistsDoNotExist(){

        SongRequest request = new SongRequest();

        Genre genre = new Genre();

        Album album = new Album();

        Song song = new Song();

        request.setGenreId(1L);

        request.setAlbumId(2L);

        request.setArtistIds(List.of(999L));

        when(songMapper.toEntity(request)).thenReturn(song);

        when(artistRepository.findById(999L)).thenReturn(Optional.empty());

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        when(albumRepository.findById(2L)).thenReturn(Optional.of(album));

        assertThrows(ResourceNotFoundException.class , () -> songService.addSong(request));

        verify(artistRepository).findById(999L);

        verify(genreRepository).findById(1L);

        verify(albumRepository).findById(2L);

        verify(songRepository , never()).save(any());

    }

    @Test
    void updateSong_shouldUpdateSong(){

        SongUpdate update = new SongUpdate();

        update.setGenreId(4L);
        update.setAlbumId(3L);
        update.setArtistIds(List.of(2L,1L));

        Song song = new Song();

        when(songRepository.findById(5L)).thenReturn(Optional.of(song));

        Genre oldGenre = mock(Genre.class);
        when(oldGenre.getId()).thenReturn(1L);

        Album oldAlbum = mock(Album.class);
        when(oldAlbum.getId()).thenReturn(2L);

        Artist oldArtist1 = mock(Artist.class);
        when(oldArtist1.getId()).thenReturn(3L);

        Artist oldArtist2 = mock(Artist.class);
        when(oldArtist2.getId()).thenReturn(4L);

        song.setGenre(oldGenre);
        song.setAlbum(oldAlbum);
        song.setArtists(new ArrayList<>(List.of(oldArtist1 , oldArtist2)));

        Genre newGenre = new Genre();
        Album newAlbum = new Album();
        Artist newArtist1 = new Artist();
        Artist newArtist2 = new Artist();

        newArtist1.setSongs(new ArrayList<>());
        newArtist2.setSongs(new ArrayList<>());

        when(genreRepository.findById(4L)).thenReturn(Optional.of(newGenre));
        when(albumRepository.findById(3L)).thenReturn(Optional.of(newAlbum));
        when(artistRepository.findById(2L)).thenReturn(Optional.of(newArtist1));
        when(artistRepository.findById(1L)).thenReturn(Optional.of(newArtist2));

        SongResponse response = new SongResponse();

        when(songMapper.toDTO(song)).thenReturn(response);

        when(cacheManager.getCache("genres")).thenReturn(cache);
        when(cacheManager.getCache("albums")).thenReturn(cache);
        when(cacheManager.getCache("artists")).thenReturn(cache);

        SongResponse result = songService.updateSongById(5L,update);

        assertEquals(response,result);

        assertEquals(newGenre , song.getGenre());
        assertEquals(newAlbum , song.getAlbum());
        assertEquals(List.of(newArtist1 , newArtist2) , song.getArtists());

        verify(songRepository).findById(5L);
        verify(genreRepository).findById(4L);
        verify(albumRepository).findById(3L);
        verify(artistRepository).findById(2L);
        verify(artistRepository).findById(1L);

        verify(songMapper).toDTO(song);

        verify(songMapper).updateSong(update,song);

    }

    @Test
    void updateSong_shouldThrowException_whenSongDoesNotExist(){

        SongUpdate update = new SongUpdate();

        when(songRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> songService.updateSongById(999L, update)
        );

        verify(songRepository).findById(999L);
    }

    @Test
    void updateSong_shouldThrowException_whenTitleIsBlank(){

        SongUpdate update = new SongUpdate();
        update.setTitle("   ");

        Song song = new Song();

        Genre oldGenre = mock(Genre.class);
        when(oldGenre.getId()).thenReturn(1L);

        Album oldAlbum = mock(Album.class);
        when(oldAlbum.getId()).thenReturn(2L);

        Artist oldArtist = mock(Artist.class);
        when(oldArtist.getId()).thenReturn(3L);

        song.setGenre(oldGenre);
        song.setAlbum(oldAlbum);
        song.setArtists(List.of(oldArtist));

        when(songRepository.findById(5L))
                .thenReturn(Optional.of(song));

        assertThrows(
                IllegalArgumentException.class,
                () -> songService.updateSongById(5L, update)
        );

        verify(songRepository).findById(5L);
    }

    @Test
    void updateSong_shouldThrowException_whenAlbumDoesNotExist(){

        SongUpdate update = new SongUpdate();
        update.setAlbumId(999L);

        Song song = new Song();

        Genre oldGenre = mock(Genre.class);
        when(oldGenre.getId()).thenReturn(1L);

        Album oldAlbum = mock(Album.class);
        when(oldAlbum.getId()).thenReturn(2L);

        Artist oldArtist = mock(Artist.class);
        when(oldArtist.getId()).thenReturn(3L);

        song.setGenre(oldGenre);
        song.setAlbum(oldAlbum);
        song.setArtists(List.of(oldArtist));

        when(songRepository.findById(5L))
                .thenReturn(Optional.of(song));

        when(albumRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> songService.updateSongById(5L, update)
        );

        verify(songRepository).findById(5L);
        verify(albumRepository).findById(999L);
    }

    @Test
    void updateSong_shouldThrowException_whenGenreDoesNotExist(){

        SongUpdate update = new SongUpdate();
        update.setGenreId(999L);

        Song song = new Song();

        Genre oldGenre = mock(Genre.class);
        when(oldGenre.getId()).thenReturn(1L);

        Album oldAlbum = mock(Album.class);
        when(oldAlbum.getId()).thenReturn(2L);

        Artist oldArtist = mock(Artist.class);
        when(oldArtist.getId()).thenReturn(3L);

        song.setGenre(oldGenre);
        song.setAlbum(oldAlbum);
        song.setArtists(List.of(oldArtist));

        when(songRepository.findById(5L))
                .thenReturn(Optional.of(song));

        when(genreRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> songService.updateSongById(5L, update)
        );

        verify(songRepository).findById(5L);
        verify(genreRepository).findById(999L);
    }

    @Test
    void updateSong_shouldThrowException_whenArtistDoesNotExist(){

        SongUpdate update = new SongUpdate();
        update.setArtistIds(List.of(999L));

        Song song = new Song();

        Genre oldGenre = mock(Genre.class);
        when(oldGenre.getId()).thenReturn(1L);

        Album oldAlbum = mock(Album.class);
        when(oldAlbum.getId()).thenReturn(2L);

        Artist oldArtist = mock(Artist.class);
        when(oldArtist.getId()).thenReturn(3L);

        song.setGenre(oldGenre);
        song.setAlbum(oldAlbum);
        song.setArtists(List.of(oldArtist));

        when(songRepository.findById(5L))
                .thenReturn(Optional.of(song));

        when(artistRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> songService.updateSongById(5L, update)
        );

        verify(songRepository).findById(5L);
        verify(artistRepository).findById(999L);
    }

    @Test
    void deleteSong_shouldDeleteSong(){

        Song song = new Song();
        Genre genre = new Genre();
        Album album = new Album();
        Artist artist = new Artist();

        song.setGenre(genre);
        song.setAlbum(album);
        song.setArtists(List.of(artist));

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        when(cacheManager.getCache(anyString())).thenReturn(cache);

        songService.deleteSongById(1L);

        verify(songRepository).findById(1L);
        verify(songRepository).delete(song);

    }

    @Test
    void deleteSong_shouldThrowException_whenSongNotFound(){

        when(songRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class , () -> songService.deleteSongById(999L));

        verify(songRepository).findById(999L);
        verify(songRepository , never()).delete((Song) any());

    }

    @Test
    void playSong_shouldIncreasePlayCount(){

        Song song = new Song();
        song.setPlayCount(0L);

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(songRepository.save(song)).thenReturn(song);

        SongResponse response = new SongResponse();

        when(songMapper.toDTO(song)).thenReturn(response);

        SongResponse result = songService.playSong(1L);

        assertEquals(response , result);
        assertEquals(1L , song.getPlayCount());

        verify(songRepository).findById(1L);
        verify(songRepository).save(song);

        verify(songMapper).toDTO(song);

    }

    @Test
    void playSong_shouldThrowException_whenSongNotFound(){

        when(songRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> songService.playSong(999L)
        );

        verify(songRepository).findById(999L);
    }

}

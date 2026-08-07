package com.resonance.resonance.mapper;

import com.resonance.resonance.dto.request.ArtistRequest;
import com.resonance.resonance.dto.response.ArtistResponse;
import com.resonance.resonance.dto.update.AlbumUpdate;
import com.resonance.resonance.dto.update.ArtistUpdate;
import com.resonance.resonance.entity.Album;
import com.resonance.resonance.entity.Artist;
import com.resonance.resonance.entity.Song;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    Artist toEntity(ArtistRequest request);

    @Mapping(source = "albums" , target = "albumTitles")
    @Mapping(source = "songs" , target = "songTitles")
    ArtistResponse toDTO(Artist artist);

    List<ArtistResponse> toDTOs(List<Artist> artists);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateArtist(ArtistUpdate update , @MappingTarget Artist artist);

    default List<String> mapSongs(List<Song> songs){

        List<String> songTitles = new ArrayList<>();

        for(Song song : songs){

            songTitles.add(song.getTitle());

        }

        return songTitles;

    }

    default List<String> mapAlbums(List<Album> albums){

        List<String> albumTitles = new ArrayList<>();

        for(Album album : albums){

            albumTitles.add(album.getTitle());

        }

        return albumTitles;

    }

}

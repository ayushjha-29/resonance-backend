package com.resonance.resonance.mapper;

import com.resonance.resonance.dto.request.SongRequest;
import com.resonance.resonance.dto.response.SongResponse;
import com.resonance.resonance.dto.update.SongUpdate;
import com.resonance.resonance.entity.Artist;
import com.resonance.resonance.entity.Song;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SongMapper {

    Song toEntity(SongRequest request);

    @Mapping(source = "album.title" , target = "albumTitle")
    @Mapping(source = "genre.name" , target = "genreName")
    @Mapping(source = "artists" , target = "artistNames")
    SongResponse toDTO(Song song);

    List<SongResponse> toDTOs(List<Song> songs);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSong(SongUpdate update , @MappingTarget Song song);

    default List<String> mapArtists(List<Artist> artists){

        List<String> artistNames = new ArrayList<>();

        for(Artist artist : artists){

            artistNames.add(artist.getName());

        }

        return artistNames;

    }
}

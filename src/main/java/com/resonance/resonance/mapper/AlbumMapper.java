package com.resonance.resonance.mapper;

import com.resonance.resonance.dto.request.AlbumRequest;
import com.resonance.resonance.dto.response.AlbumResponse;
import com.resonance.resonance.dto.update.AlbumUpdate;
import com.resonance.resonance.entity.Album;
import com.resonance.resonance.entity.Song;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    Album toEntity(AlbumRequest request);

    @Mapping(source = "artist.name" , target = "artistName")
    @Mapping(source = "songs" , target = "songTitles")
    AlbumResponse toDTO(Album album);

    List<AlbumResponse> toDTOs(List<Album> albums);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAlbum(AlbumUpdate update , @MappingTarget Album album);

    default List<String> mapSongs(List<Song> songs){

        List<String> songTitles = new ArrayList<>();

        for(Song song : songs){

            songTitles.add(song.getTitle());

        }

        return songTitles;

    }

}

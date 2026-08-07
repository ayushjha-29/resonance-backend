package com.resonance.resonance.mapper;

import com.resonance.resonance.dto.request.GenreRequest;
import com.resonance.resonance.dto.response.GenreResponse;
import com.resonance.resonance.dto.update.GenreUpdate;
import com.resonance.resonance.entity.Genre;
import com.resonance.resonance.entity.Song;
import org.mapstruct.*;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    Genre toEntity(GenreRequest request);

    @Mapping(source = "songs" , target = "songTitles")
    GenreResponse toDTO(Genre genre);

    List<GenreResponse> toDTOs(List<Genre> genres);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateGenre(GenreUpdate update , @MappingTarget Genre genre);

    default List<String> mapSongs(List<Song> songs){

        List<String> songTitles = new ArrayList<>();

        for(Song song : songs){

            songTitles.add(song.getTitle());

        }

        return songTitles;

    }

}

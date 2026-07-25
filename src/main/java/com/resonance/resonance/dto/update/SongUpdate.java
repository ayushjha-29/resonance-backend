package com.resonance.resonance.dto.update;

import com.resonance.resonance.enums.Language;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SongUpdate {

    private String title;
    private Integer duration;
    private LocalDate releaseDate;
    private Language language;

    private List<Long> artistIds;

    private Long genreId;

    private Long albumId;

}

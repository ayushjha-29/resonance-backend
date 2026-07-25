package com.resonance.resonance.dto.response;

import com.resonance.resonance.enums.Language;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SongResponse {

    private Long id;

    private String title;
    private Integer duration;
    private LocalDate releaseDate;

    private Language language;

    private Long playCount;

    private List<String> artistNames;

    private String albumTitle;

    private String genreName;

}

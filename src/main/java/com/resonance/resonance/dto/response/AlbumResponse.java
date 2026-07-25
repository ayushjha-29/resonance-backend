package com.resonance.resonance.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AlbumResponse {

    private Long id;

    private String title;
    private String description;
    private LocalDate releaseDate;
    private String artistName;
    private List<String> songTitles;

}

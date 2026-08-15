package com.resonance.resonance.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AlbumFilter {

    private String title;
    private LocalDate minReleaseDate;
    private LocalDate maxReleaseDate;
    private String artistName;
    private String songTitle;

}

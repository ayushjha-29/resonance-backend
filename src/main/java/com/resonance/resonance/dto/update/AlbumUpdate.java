package com.resonance.resonance.dto.update;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AlbumUpdate {

    private String title;
    private String description;
    private LocalDate releaseDate;

    private Long artistId;

}

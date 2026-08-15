package com.resonance.resonance.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SongFilter {

    private String title;
    private Integer minDuration;
    private Integer maxDuration;
    private Long minPlayCount;
    private Long maxPlayCount;
    private String genreName;
    private String artistName;
    private String albumTitle;
}

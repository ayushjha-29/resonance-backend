package com.resonance.resonance.dto.request;

import com.resonance.resonance.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SongRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Positive(message = "Duration cannot be negative")
    @NotNull(message = "Duration cannot be empty")
    private Integer duration;

    @NotNull(message = "Release Date cannot be empty")
    private LocalDate releaseDate;

    @NotNull(message = "Language cannot be blank")
    private Language language;

    private List<Long> artistIds;

    private Long genreId;

    private Long albumId;
}

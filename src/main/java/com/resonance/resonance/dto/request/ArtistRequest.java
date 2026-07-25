package com.resonance.resonance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
public class ArtistRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Bio cannot be blank")
    private String bio;

    @NotBlank(message = "Country cannot be blank")
    private String country;

    @Positive(message = "Debut year cannot be negative")
    @NotNull(message = "Debut Year cannot be empty")
    private Integer debutYear;


}

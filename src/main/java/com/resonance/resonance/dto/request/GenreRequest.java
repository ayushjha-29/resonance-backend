package com.resonance.resonance.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenreRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

}

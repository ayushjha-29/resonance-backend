package com.resonance.resonance.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GenreResponse {

    private Long id;
    private String name;
    private String description;
    private List<String> songTitles;

}

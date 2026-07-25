package com.resonance.resonance.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ArtistResponse {

    private Long id;

    private String name;
    private String bio;
    private String country;
    private Integer debutYear;
    private List<String> albumTitles;
    private List<String> songTitles;

}

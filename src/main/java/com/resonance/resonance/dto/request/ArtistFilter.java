package com.resonance.resonance.dto.request;

import lombok.Data;

@Data
public class ArtistFilter {

    private String name;
    private String country;
    private String albumTitle;
    private String songTitle;
}

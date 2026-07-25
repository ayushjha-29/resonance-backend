package com.resonance.resonance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String title;
    private String description;
    private LocalDate releaseDate;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @OneToMany(
            mappedBy = "album",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Song> songs = new ArrayList<>();

}

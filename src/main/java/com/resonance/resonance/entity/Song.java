package com.resonance.resonance.entity;

import com.resonance.resonance.enums.Language;
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
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String title;
    private Integer duration;
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    private Language language;

    private Long playCount;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToMany(
            mappedBy = "songs"
    )
    private List<Artist> artists = new ArrayList<>();

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "album_id")
    private Album album;

}

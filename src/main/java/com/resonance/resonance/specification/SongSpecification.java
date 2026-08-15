package com.resonance.resonance.specification;

import com.resonance.resonance.dto.request.SongFilter;
import com.resonance.resonance.entity.Album;
import com.resonance.resonance.entity.Artist;
import com.resonance.resonance.entity.Genre;
import com.resonance.resonance.entity.Song;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class SongSpecification {

    public static Specification<Song> findByTitle(String title) {

        return (root, query, cb) -> cb.equal(cb.lower(root.get("title")), title.toLowerCase());

    }


    public static Specification<Song> durationGreaterThanOrEqualTo(Integer minDuration) {

        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("duration"), minDuration);

    }

    public static Specification<Song> durationLessThanOrEqualTo(Integer maxDuration) {

        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("duration"), maxDuration);

    }

    public static Specification<Song> playCountGreaterThanOrEqualTo(Long minPlayCount) {

        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("playCount"), minPlayCount);

    }

    public static Specification<Song> playCountLessThanOrEqualTo(Long maxPlaycount) {

        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("playCount"), maxPlaycount);

    }

    public static Specification<Song> findByGenreName(String genreName) {

        return (root, query, cb) -> {

            Join<Song, Genre> genreJoin = root.join("genre");

            return cb.equal(cb.lower(genreJoin.get("name")), genreName.toLowerCase());

        };
    }

    public static Specification<Song> findByArtistName(String artistName) {

        return (root, query, cb) -> {

            Join<Song, Artist> artistJoin = root.join("artists");

            return cb.equal(cb.lower(artistJoin.get("name")), artistName.toLowerCase());

        };
    }

    public static Specification<Song> findByAlbumTitle(String albumTitle) {

        return (root, query, cb) -> {

            Join<Song, Album>  albumJoin = root.join("album");

            return cb.equal(cb.lower(albumJoin.get("title")), albumTitle.toLowerCase());

        };
    }

    public static Specification<Song> buildSpecification(SongFilter filter){

        Specification<Song> specification = (root , query , cb) -> cb.conjunction();

        if(filter.getTitle() != null){

            specification = specification.and(findByTitle(filter.getTitle()));

        }

        if(filter.getMinDuration() != null){

            specification = specification.and(durationGreaterThanOrEqualTo(filter.getMinDuration()));

        }

        if(filter.getMaxDuration() != null){

            specification = specification.and(durationLessThanOrEqualTo(filter.getMaxDuration()));

        }

        if(filter.getMinPlayCount() != null){

            specification = specification.and(playCountGreaterThanOrEqualTo(filter.getMinPlayCount()));

        }

        if(filter.getMaxPlayCount() != null){

            specification = specification.and(playCountLessThanOrEqualTo(filter.getMaxPlayCount()));

        }

        if(filter.getGenreName() != null){

            specification = specification.and(findByGenreName(filter.getGenreName()));

        }

        if(filter.getArtistName() != null){

            specification = specification.and(findByArtistName(filter.getArtistName()));

        }

        if(filter.getAlbumTitle() != null){

            specification = specification.and(findByAlbumTitle(filter.getAlbumTitle()));

        }

        return specification;

    }

}

package com.resonance.resonance.specification;

import com.resonance.resonance.dto.request.AlbumFilter;
import com.resonance.resonance.entity.Album;
import com.resonance.resonance.entity.Artist;
import com.resonance.resonance.entity.Song;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class AlbumSpecification {

    public static Specification<Album> findByTitle(String title){

        return (root , query , cb) -> cb.equal(cb.lower(root.get("title")),title.toLowerCase());

    }

    public static Specification<Album> releaseDateGreaterThanOrEqualTo(LocalDate minReleaseDate){

        return (root , query , cb) -> cb.greaterThanOrEqualTo(root.get("releaseDate"),minReleaseDate);

    }

    public static Specification<Album> releaseDateLessThanOrEqualTo(LocalDate maxReleaseDate){

        return (root , query , cb) -> cb.lessThanOrEqualTo(root.get("releaseDate"),maxReleaseDate);

    }

    public static Specification<Album> findByArtistName(String artistName){

        return (root , query , cb) ->{

            Join<Album, Artist> artistJoin = root.join("artist");

            return cb.equal(cb.lower(artistJoin.get("name")),artistName.toLowerCase());

        };

    }

    public static Specification<Album> findBySongTitle(String songTitle){

        return (root , query , cb) ->{

            Join<Album, Song> songJoin = root.join("songs");

            return cb.equal(cb.lower(songJoin.get("title")),songTitle.toLowerCase());

        };

    }

    public static Specification<Album> buildSpecification(AlbumFilter filter){

        Specification<Album> specification = (root , query , cb) -> cb.conjunction();

        if(filter.getTitle() != null){

            specification = specification.and(findByTitle(filter.getTitle()));

        }

        if(filter.getSongTitle() != null){

            specification = specification.and(findBySongTitle(filter.getSongTitle()));

        }

        if(filter.getMinReleaseDate() != null){

            specification = specification.and(releaseDateGreaterThanOrEqualTo(filter.getMinReleaseDate()));

        }

        if(filter.getMaxReleaseDate() != null){

            specification = specification.and(releaseDateLessThanOrEqualTo(filter.getMaxReleaseDate()));

        }

        if(filter.getArtistName() !=null){

            specification = specification.and(findByArtistName(filter.getArtistName()));

        }

        return specification;

    }

}

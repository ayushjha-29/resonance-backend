package com.resonance.resonance.specification;

import com.resonance.resonance.dto.request.ArtistFilter;
import com.resonance.resonance.entity.Album;
import com.resonance.resonance.entity.Artist;
import com.resonance.resonance.entity.Song;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ArtistSpecification {

    public static Specification<Artist> findByName(String name){

        return (root , query , cb) -> cb.equal(cb.lower(root.get("name")),name.toLowerCase());

    }

    public static Specification<Artist> findByCountry(String country){

        return (root , query , cb) -> cb.equal(cb.lower(root.get("country")),country.toLowerCase());

    }

    public static Specification<Artist> findByAlbumTitle(String albumTitle){

        return (root , query , cb) ->{

            Join<Artist, Album> albumJoin = root.join("albums");

            return cb.equal(cb.lower(albumJoin.get("title")),albumTitle.toLowerCase());

        };

    }

    public static Specification<Artist> findBySongTitle(String songTitle){

        return (root , query , cb) ->{

            Join<Artist, Song> songJoin = root.join("songs");

            return cb.equal(cb.lower(songJoin.get("title")),songTitle.toLowerCase());

        };

    }

    public static Specification<Artist> buildSpecification(ArtistFilter filter){

        Specification<Artist> specification = (root , query , cb) -> cb.conjunction();

        if(filter.getAlbumTitle() != null){

            specification = specification.and(findByAlbumTitle(filter.getAlbumTitle()));

        }

        if(filter.getName() != null){

            specification = specification.and(findByName(filter.getName()));

        }

        if(filter.getCountry() != null){

            specification = specification.and(findByCountry(filter.getCountry()));

        }

        if(filter.getSongTitle() != null){

            specification = specification.and(findBySongTitle(filter.getSongTitle()));

        }

        return specification;

    }

}

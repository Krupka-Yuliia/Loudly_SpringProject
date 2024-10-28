package com.loudlyapp.artist;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ArtistService {
    private final ArtistRepository artistRepository;


    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Optional<Artist> findById(long id) {
        return artistRepository.findById(id);
    }

    public Artist save(Artist artist) {
        return artistRepository.save(artist);
    }

    public void deleteAll() {
        artistRepository.deleteAll();
    }

    public Artist updateArtist(long id, Artist artist) {
        return artistRepository.findById(id)
                .map(existingArtist -> {
                    existingArtist.setNickname(artist.getNickname());
                    existingArtist.setBiography(artist.getBiography());
                    return artistRepository.save(existingArtist);
                })
                .orElseThrow(() -> new EntityNotFoundException("Artist with id " + id + " not found"));
    }

    public void deleteArtistById(long id) {
        artistRepository.deleteById(id);
    }

    public Optional<Artist> findByName(String name) {
        return artistRepository.findByNickname(name);
    }
}


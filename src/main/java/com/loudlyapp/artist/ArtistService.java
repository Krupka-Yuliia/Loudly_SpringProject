package com.loudlyapp.artist;

import com.loudlyapp.user.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ArtistService {
    private final ArtistRepository artistRepository;

    private ArtistDTO convertToDTO(Artist artist) {
        if (artist == null) {
            return null;
        }
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setId(artist.getId());
        artistDTO.setBiography(artist.getBiography());
        artistDTO.setNickname(artist.getNickname());
        return artistDTO;
    }

    private Artist convertToEntity(ArtistDTO artistDTO) {
        if (artistDTO == null) {
            return null;
        }
        Artist artist = new Artist();
        artist.setId(artistDTO.getId());
        artist.setBiography(artistDTO.getBiography());
        artist.setNickname(artistDTO.getNickname());
        return artist;
    }

    public String getArtistNameById(int artistId) {
        Optional<Artist> artistOptional = artistRepository.findById((long) artistId);
        return artistOptional.map(Artist::getNickname).orElse(null);
    }

    public List<ArtistDTO> getAllArtists() {
        return artistRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Optional<ArtistDTO> findById(long id) {
        return artistRepository.findById(id)
                .map(this::convertToDTO);
    }

    public ArtistDTO save(ArtistDTO artistDTO) {
        Artist artist = convertToEntity(artistDTO);
        Artist savedArtist = artistRepository.save(artist);
        return convertToDTO(savedArtist);
    }

    public void deleteAll() {
        artistRepository.deleteAll();
    }

    public ArtistDTO updateArtist(long id, ArtistDTO artistDTO) {
        return artistRepository.findById(id)
                .map(existingArtist -> {
                    existingArtist.setNickname(artistDTO.getNickname());
                    existingArtist.setBiography(artistDTO.getBiography());
                    return artistRepository.save(existingArtist);
                })
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Artist with id " + id + " not found"));
    }

    public void deleteArtistById(long id) {
        artistRepository.deleteById(id);
    }

    public Optional<ArtistDTO> findByName(String name) {
        return artistRepository.findByNickname(name)
                .map(this::convertToDTO);
    }
}

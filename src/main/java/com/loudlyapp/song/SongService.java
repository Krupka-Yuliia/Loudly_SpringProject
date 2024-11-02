package com.loudlyapp.song;

import com.loudlyapp.artist.ArtistService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SongService {

    private final SongRepository songRepository;
    private final ArtistService artistService;

    private SongDTO convertToDTO(Song song) {
        if (song == null) {
            return null;
        }
        SongDTO songDTO = new SongDTO();
        songDTO.setId(song.getId());
        songDTO.setTitle(song.getTitle());
        songDTO.setArtistId(song.getArtistId());

        String artistName = artistService.getArtistNameById(song.getArtistId());
        songDTO.setArtistName(artistName);

        songDTO.setFormat(song.getFormat());
        songDTO.setGenre(song.getGenre());
        songDTO.setYear(song.getYear());
        return songDTO;
    }

    private Song convertToEntity(SongDTO songDTO) {
        if (songDTO == null) {
            return null;
        }
        Song song = new Song();
        song.setId(songDTO.getId());
        song.setTitle(songDTO.getTitle());
        song.setArtistId(songDTO.getArtistId());
        song.setFormat(songDTO.getFormat());
        song.setGenre(songDTO.getGenre());
        song.setYear(songDTO.getYear());
        return song;
    }

    public List<SongDTO> findAll() {
        return songRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteAll() {
        songRepository.deleteAll();
    }

    public Optional<SongDTO> findById(long id) {
        return songRepository.findById(id)
                .map(this::convertToDTO);
    }

    public SongDTO save(SongDTO songDTO) {
        Song song = convertToEntity(songDTO);
        Song savedSong = songRepository.save(song);
        return convertToDTO(savedSong);
    }

    public void deleteById(Long id) {
        songRepository.deleteById(id);
    }

    public List<SongDTO> findByTitle(String name) {
        return songRepository.findByTitle(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SongDTO> findByGenre(String genre) {
        return songRepository.findByGenre(genre).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SongDTO> findByArtistId(int artistId) {
        return songRepository.findByArtistId(artistId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SongDTO updateSong(long id, SongDTO songDTO) {
        return songRepository.findById(id)
                .map(existingSong -> {
                    existingSong.setTitle(songDTO.getTitle());
                    existingSong.setGenre(songDTO.getGenre());
                    existingSong.setArtistId(songDTO.getArtistId());
                    Song updatedSong = songRepository.save(existingSong);
                    return convertToDTO(updatedSong);
                })
                .orElseThrow(() -> new EntityNotFoundException("Song with id " + id + " not found"));
    }
}

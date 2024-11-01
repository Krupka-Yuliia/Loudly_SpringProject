package com.loudlyapp.song;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SongService {

    private final SongRepository songRepository;


    public List<Song> findAll() {
        return songRepository.findAll();
    }

    public void deleteAll(){
        songRepository.deleteAll();
    }

    public Optional<Song> findById(long id) {
        return songRepository.findById(id);
    }

    public Song save(Song song) {
        return songRepository.save(song);
    }

    public void deleteById(Long id) {
        songRepository.deleteById(id);
    }

    public List<Song> findByTitle(String name) {
        return songRepository.findByTitle(name);
    }

    public List<Song> findByGenre(String genre) {
        return songRepository.findByGenre(genre);
    }

    public List<Song> findByArtistId(int artistId) {
        return songRepository.findByArtistId(artistId);
    }

    public Song updateSong(long id, Song song) {
        return songRepository.findById(id)
                .map(existingSong -> {
                    existingSong.setTitle(song.getTitle());
                    existingSong.setGenre(song.getGenre());
                    existingSong.setArtistId(song.getArtistId());
                    return songRepository.save(existingSong);
                })
                .orElseThrow(() -> new EntityNotFoundException("Song with id " + id + " not found"));
    }

}

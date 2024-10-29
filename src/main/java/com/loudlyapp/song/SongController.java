package com.loudlyapp.song;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/songs")
@AllArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping
    public List<Song> getAllSongs() {
        return songService.findAll();
    }

    @PostMapping
    public Song createSong(@RequestBody Song song) {
        return songService.save(song);
    }

    @GetMapping("/{songId}")
    public Optional<Song> getSongById(@PathVariable Long songId) {
        return songService.findById(songId);
    }

    @DeleteMapping("/{songId}")
    public void deleteSongById(@PathVariable Long songId) {
        songService.deleteById(songId);
    }

    @GetMapping("/search")
    public List<Song> searchSongsByTitle(@RequestParam String title) {
        List<Song> songs = songService.findByTitle(title);
        return songs;
    }

    @GetMapping("/searchByGenre")
    public List<Song> searchSongsByGenre(@RequestParam String genre) {
        List<Song> songs = songService.findByGenre(genre);
        return songs;
    }

    @GetMapping("/searchByArtistId")
    public List<Song> searchSongsByArtistId(@RequestParam int artistId) {
        List<Song> songs = songService.findByArtistId(artistId);
        return songs;
    }

    @PutMapping("/{songId}")
    public Song updateSong(@PathVariable Long songId, @RequestBody Song song) {
        Song updatedSong = songService.updateSong(songId, song);
        return updatedSong;
    }

    @DeleteMapping
    public void deleteAllSongs() {
        songService.deleteAll();
    }

}

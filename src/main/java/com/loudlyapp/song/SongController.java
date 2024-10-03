package com.loudlyapp.song;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Song addSong(@RequestBody Song song) {
        return songService.save(song);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Song>> searchSongsByTitle(@RequestParam String title) {
        List<Song> songs = songService.findByTitle(title);
        if (songs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/searchByGenre")
    public ResponseEntity<List<Song>> searchSongsByGenre(@RequestParam String genre) {
        List<Song> songs = songService.findByGenre(genre);
        if (songs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/searchByArtistId")
    public ResponseEntity<List<Song>> searchSongsByArtistId(@RequestParam String artistId) {
        List<Song> songs = songService.findByArtistId(artistId);
        if (songs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(songs);
    }

    @PutMapping("/{songId}")
    public ResponseEntity<Song> updateSong(@PathVariable Long songId, @RequestBody Song song) {
        Song updatedSong = songService.updateSong(songId, song);
        return ResponseEntity.ok(updatedSong);
    }

}

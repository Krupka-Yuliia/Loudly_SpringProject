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
    public List<SongDTO> getAllSongs() {
        return songService.findAll();
    }

    @PostMapping
    public SongDTO createSong(@RequestBody SongDTO songDTO) {
        return songService.save(songDTO);
    }

    @GetMapping("/{songId}")
    public Optional<SongDTO> getSongById(@PathVariable Long songId) {
        return songService.findById(songId);
    }

    @DeleteMapping("/{songId}")
    public void deleteSongById(@PathVariable Long songId) {
        songService.deleteById(songId);
    }

    @GetMapping("/search")
    public List<SongDTO> searchSongsByTitle(@RequestParam String title) {
        return songService.findByTitle(title);
    }

    @GetMapping("/searchByGenre")
    public List<SongDTO> searchSongsByGenre(@RequestParam String genre) {
        return songService.findByGenre(genre);
    }

    @GetMapping("/searchByArtistId")
    public List<SongDTO> searchSongsByArtistId(@RequestParam int artistId) {
        return songService.findByArtistId(artistId);
    }

    @PutMapping("/{songId}")
    public SongDTO updateSong(@PathVariable Long songId, @RequestBody SongDTO songDTO) {
        return songService.updateSong(songId, songDTO);
    }

    @DeleteMapping
    public void deleteAllSongs() {
        songService.deleteAll();
    }
}

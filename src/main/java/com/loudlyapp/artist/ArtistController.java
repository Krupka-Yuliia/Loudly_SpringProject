package com.loudlyapp.artist;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/artists")
@AllArgsConstructor
public class ArtistController {


    private final ArtistService artistService;


    @GetMapping()
    public List<Artist> getArtists() {
        return artistService.getAllArtists();
    }

    @GetMapping("/{artistId}")
    public Optional<Artist> getArtist(@PathVariable Long artistId) {
        return artistService.findById(artistId);
    }

    @PostMapping()
    public Artist createArtist(@RequestBody Artist artist) {
        return artistService.save(artist);
    }

    @DeleteMapping
    public void deleteAll() {
        artistService.deleteAll();
    }

    @PutMapping("/{artistId}")
    public ResponseEntity<Artist> updateArtist(@PathVariable Long artistId, @RequestBody Artist artist) {
        Artist updatedArtist = artistService.updateArtist(artistId, artist);
        return ResponseEntity.ok(updatedArtist);
    }

    @DeleteMapping("/{artistId}")
    public void deleteArtist(@PathVariable Long artistId) {
        artistService.deleteArtistById(artistId);
    }

    @GetMapping("/search")
    public Optional<Artist> getArtistByName(@RequestParam String name) {
        return artistService.findByName(name);
    }
}



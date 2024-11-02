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

    @GetMapping
    public List<ArtistDTO> getArtists() {
        return artistService.getAllArtists();
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistDTO> getArtist(@PathVariable Long artistId) {
        return artistService.findById(artistId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArtistDTO> createArtist(@RequestBody ArtistDTO artistDTO) {
        ArtistDTO createdArtist = artistService.save(artistDTO);
        return ResponseEntity.status(201).body(createdArtist);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        artistService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{artistId}")
    public ResponseEntity<ArtistDTO> updateArtist(@PathVariable Long artistId, @RequestBody ArtistDTO artistDTO) {
        ArtistDTO updatedArtist = artistService.updateArtist(artistId, artistDTO);
        return ResponseEntity.ok(updatedArtist);
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long artistId) {
        artistService.deleteArtistById(artistId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<ArtistDTO> getArtistByName(@RequestParam String nickname) {
        return artistService.findByName(nickname)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

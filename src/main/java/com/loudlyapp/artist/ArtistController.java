package com.loudlyapp.artist;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/artists")
@AllArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping
    public List<ArtistDTO> getArtists() {
        return artistService.getAllArtists();
    }

    @GetMapping("/{artistId}")
    public Optional<ArtistDTO> getArtist(@PathVariable Long artistId) {
        return artistService.findById(artistId);
    }

    @PostMapping
    public ArtistDTO createArtist(@RequestBody ArtistDTO artistDTO) {
        return artistService.save(artistDTO);
    }

    @DeleteMapping
    public void deleteAll() {
        artistService.deleteAll();
    }

    @PutMapping("/{artistId}")
    public ArtistDTO updateArtist(@PathVariable Long artistId, @RequestBody ArtistDTO artistDTO) {
        return artistService.updateArtist(artistId, artistDTO);
    }

    @DeleteMapping("/{artistId}")
    public void deleteArtist(@PathVariable Long artistId) {
        artistService.deleteArtistById(artistId);
    }

    @GetMapping("/search")
    public Optional<ArtistDTO> getArtistByName(@RequestParam String nickname) {
        return artistService.findByName(nickname);
    }
}

package com.loudlyapp.artist;

import lombok.AllArgsConstructor;
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
}


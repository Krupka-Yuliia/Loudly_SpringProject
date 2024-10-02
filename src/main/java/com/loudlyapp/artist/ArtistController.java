package com.loudlyapp.artist;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}


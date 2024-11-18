package com.loudlyapp.web_controllers;

import com.loudlyapp.artist.ArtistDTO;
import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.song.SongDTO;
import com.loudlyapp.song.SongService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class ArtistWebController {

    private final SongService songService;
    private final ArtistService artistService;

    @GetMapping("/artists/show/{id}")
    public String getArtist(@PathVariable Long id, Model model) {
        Optional<ArtistDTO> artistDTO = artistService.findById(id);

        if (artistDTO.isPresent()) {
            ArtistDTO artist = artistDTO.get();
            model.addAttribute("artist", artist);
            List<SongDTO> songs = songService.findByArtistId(artist.getId());
            model.addAttribute("songs", songs);
        }
        return "artist";
    }

    @GetMapping("/artists/show")
    public String getAllArtists(Model model) {
        List<ArtistDTO> artists = artistService.getAllArtists();
        model.addAttribute("artists", artists);
        return "artists";
    }
}

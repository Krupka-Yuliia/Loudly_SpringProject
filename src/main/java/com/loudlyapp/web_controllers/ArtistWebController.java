package com.loudlyapp.web_controllers;

import com.loudlyapp.artist.Artist;
import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.song.Song;
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

    @GetMapping("/artist/{id}")
    public String getArtist(@PathVariable Long id, Model model) {
        Optional<Artist> artistOptional = artistService.findById(id);

        if (artistOptional.isPresent()) {
            Artist artist = artistOptional.get();
            model.addAttribute("artist", artist);

            List<Song> songs = songService.findByArtistId(artist.getId());

            for (Song song : songs) {
                song.setArtistName(artist.getNickname());
            }

            model.addAttribute("songs", songs);
        }

        return "artist";
    }

    @GetMapping("/artist")
    public String getAllArtists(Model model) {
        List<Artist> artists = artistService.getAllArtists();
        model.addAttribute("artists", artists);
        return "artists";
    }
}

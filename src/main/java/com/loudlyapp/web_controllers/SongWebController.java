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
public class SongWebController {

    private final SongService songService;
    private final ArtistService artistService;

    @GetMapping("/songs/show/{id}")
    public String getSongPage(@PathVariable Long id, Model model) {
        Optional<SongDTO> songOptional = songService.findById(id);

        if (songOptional.isPresent()) {
            SongDTO song = songOptional.get();
            Optional<ArtistDTO> artistOptional = artistService.findById(song.getArtistId());

            if (artistOptional.isPresent()) {
                ArtistDTO artist = artistOptional.get();
                song.setArtistName(artist.getNickname());
                model.addAttribute("artistId", artist.getId());
            } else {
                model.addAttribute("error", "Artist not found");
            }

            model.addAttribute("songName", song.getTitle());
            model.addAttribute("artist", song.getArtistName());
            model.addAttribute("year", song.getYear());
            model.addAttribute("genre", song.getGenre());
        } else {
            model.addAttribute("error", "Song not found");
        }
        return "song";
    }

    @GetMapping("/songs/show")
    public String getAllSongs(Model model) {
        List<SongDTO> songs = songService.findAll();
        model.addAttribute("songs", songs);
        return "songs";
    }
}

package com.loudlyapp.web_controllers;


import com.loudlyapp.artist.Artist;
import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.playlist.PlayListService;
import com.loudlyapp.playlist.Playlist;
import com.loudlyapp.song.Song;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@AllArgsConstructor
@Controller
public class PlaylistWebController {

    private final PlayListService playListService;
    private final ArtistService artistService;


    @GetMapping("/playlists/show/{id}")
    public String getPlaylist(Model model, @PathVariable long id) {
        Optional<Playlist> playlist = playListService.findById(id);

        if (playlist.isPresent()) {
            Playlist currentPlaylist = playlist.get();

            for (Song song : currentPlaylist.getSongs()) {
                Optional<Artist> artist = artistService.findById(song.getArtistId());
                artist.ifPresent(value -> song.setArtistName(value.getNickname()));
            }

            model.addAttribute("playlist", currentPlaylist);
        }

        return "playlist";
    }
}

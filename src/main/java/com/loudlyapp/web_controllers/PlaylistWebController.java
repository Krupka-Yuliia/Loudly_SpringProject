package com.loudlyapp.web_controllers;

import com.loudlyapp.artist.ArtistDTO;
import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.playlist.PlayListService;
import com.loudlyapp.playlist.PlaylistDTO;
import com.loudlyapp.song.SongDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class PlaylistWebController {

    private final PlayListService playListService;
    private final ArtistService artistService;

    @GetMapping("/playlists/show/{id}")
    public String getPlaylist(Model model, @PathVariable long id) {
        Optional<PlaylistDTO> playlistOpt = playListService.findById(id);


        PlaylistDTO currentPlaylist = playlistOpt.get();

        currentPlaylist.getSongs().forEach(song -> {
            Optional<ArtistDTO> artistOpt = artistService.findById(song.getArtistId());
            artistOpt.ifPresent(artist -> song.setArtistName(artist.getNickname()));
        });

        model.addAttribute("playlist", currentPlaylist);
        return "playlist";

    }
}

package com.loudlyapp.web_controllers;

import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.song.Song;
import com.loudlyapp.artist.Artist;
import com.loudlyapp.song.SongService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@AllArgsConstructor
@Controller
public class SongWebController {

    private final SongService songService;
    private final ArtistService artistService;

    @GetMapping("/song/{id}")
    public String getSongPage(@PathVariable Long id, Model model) {
        Optional<Song> songOptional = songService.findById(id);

        if (songOptional.isPresent()) {
            Song song = songOptional.get();
            Optional<Artist> artistOptional = artistService.findById(song.getArtistId());

            if (artistOptional.isPresent()) {
                Artist artist = artistOptional.get();
                song.setArtistName(artist.getNickname());
                model.addAttribute("artistId", artist.getId());
            } else {
                model.addAttribute("error", "Artist not found");
            }

            model.addAttribute("songName", song.getTitle());
            model.addAttribute("artist", song.getArtistName());
            model.addAttribute("year", song.getYear());
            model.addAttribute("genre", song.getGenre());
        }
        return "song";
    }

    @GetMapping("/song")
    public String getAllSongs(Model model) {
        List<Song> songs = songService.findAll();
        List<Map<String, Object>> songList = new ArrayList<>();

        for (Song song : songs) {
            Optional<Artist> artistOptional = artistService.findById(song.getArtistId());
            if (artistOptional.isPresent()) {
                song.setArtistName(artistOptional.get().getNickname());
            }

            Map<String, Object> songData = new HashMap<>();
            songData.put("id", song.getId());
            songData.put("title", song.getTitle());
            songData.put("artist", song.getArtistName());
            songData.put("year", song.getYear());
            songData.put("genre", song.getGenre());
            songList.add(songData);
        }

        model.addAttribute("songs", songList);
        return "songs";
    }
}

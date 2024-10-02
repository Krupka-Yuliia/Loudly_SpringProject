package com.loudlyapp.song;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
@AllArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping
    public List<Song> getAllSongs() {
        return songService.findAll();
    }

    @PostMapping
    public Song addSong(@RequestBody Song song) {
        return songService.save(song);
    }

}

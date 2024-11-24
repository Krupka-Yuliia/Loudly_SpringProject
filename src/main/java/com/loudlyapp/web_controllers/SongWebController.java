package com.loudlyapp.web_controllers;

import com.loudlyapp.artist.ArtistDTO;
import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.song.SongDTO;
import com.loudlyapp.song.SongService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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

    @GetMapping("/admin/songs/upload")
    public String showUploadForm() {
        return "add_song_form";
    }

    @PostMapping("/admin/songs/upload")
    public String uploadSong(@RequestParam("file") MultipartFile file,
                             @RequestParam("title") String title,
                             @RequestParam("artistId") int artistId,
                             @RequestParam("genre") String genre,
                             @RequestParam("year") int year,
                             Model model) throws IOException {

        byte[] fileBytes = file.getBytes();

        SongDTO songDTO = new SongDTO();
        songDTO.setTitle(title);
        songDTO.setArtistId(artistId);
        songDTO.setGenre(genre);
        songDTO.setYear(year);
        songDTO.setFile(fileBytes);

        SongDTO savedSong = songService.save(songDTO);

        model.addAttribute("message", "Song uploaded successfully!");
        return "redirect:/songs/show/" + savedSong.getId();
    }

    @GetMapping("/songs/play/{id}")
    public ResponseEntity<InputStreamResource> getSongAudio(@PathVariable Long id) throws IOException {
        Optional<SongDTO> songOptional = songService.findById(id);
        if (songOptional.isPresent()) {
            SongDTO song = songOptional.get();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(song.getFile());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + song.getTitle() + ".mp3\"")
                    .contentType(MediaType.valueOf("audio/mpeg"))
                    .body(new InputStreamResource(byteArrayInputStream));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

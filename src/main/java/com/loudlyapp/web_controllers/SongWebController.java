package com.loudlyapp.web_controllers;

import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.playlist.PlayListService;
import com.loudlyapp.playlist.PlaylistDTO;
import com.loudlyapp.song.SongDTO;
import com.loudlyapp.song.SongService;
import com.loudlyapp.user.UserDTO;
import com.loudlyapp.user.UserService;
import com.loudlyapp.utils.InvalidInputException;
import com.loudlyapp.utils.SongNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class SongWebController {

    private final SongService songService;
    private final UserService userService;
    private final PlayListService playlistService;

    @GetMapping("/songs/{id}")
    public String getSongPage(@AuthenticationPrincipal User principal, @PathVariable Long id, Model model) {
        Optional<SongDTO> songOptional = songService.findById(id);
        Optional<UserDTO> userOptional = userService.findByUsername(principal.getUsername());

        if (songOptional.isPresent()) {
            SongDTO song = songOptional.get();
            model.addAttribute("songName", song.getTitle());
            model.addAttribute("artist", song.getArtistName());
            model.addAttribute("artistId", song.getArtistId());
            model.addAttribute("year", song.getYear());
            model.addAttribute("genre", song.getGenre());
            model.addAttribute("id", song.getId());

            if (userOptional.isPresent()) {
                List<PlaylistDTO> playlists = playlistService.findPlaylistsByUserId(userOptional.get().getId());
                model.addAttribute("playlists", playlists);
            }
        } else {
            throw new SongNotFoundException(id);
        }
        return "song";
    }


    @GetMapping("/songs")
    public String getAllSongs(Model model) {
        List<SongDTO> songs = songService.findAll();
        songs.sort(Comparator.comparing(SongDTO::getTitle));
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
                             @RequestParam("year") int year) throws IOException {

        if (file.isEmpty() || title.isEmpty() || genre.isEmpty() || artistId == 0 || year == 0) {
            throw new InvalidInputException("All fields are required for uploading a song.");
        }

        byte[] fileBytes = file.getBytes();

        SongDTO songDTO = new SongDTO();
        songDTO.setTitle(title);
        songDTO.setArtistId(artistId);
        songDTO.setGenre(genre);
        songDTO.setYear(year);
        songDTO.setFile(fileBytes);

        SongDTO savedSong = songService.save(songDTO);
        return "redirect:/songs/" + savedSong.getId();
    }


    @GetMapping("/songs/play/{id}")
    public ResponseEntity<InputStreamResource> getSongAudio(@PathVariable Long id) throws IOException {
        Optional<SongDTO> songOptional = songService.findById(id);
        if (songOptional.isPresent()) {
            SongDTO song = songOptional.get();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(song.getFile());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + song.getTitle() + ".mp3\"");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");
            headers.add(HttpHeaders.PRAGMA, "no-cache");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.valueOf("audio/mpeg"))
                    .body(new InputStreamResource(byteArrayInputStream));
        } else {
            throw new SongNotFoundException(id);
        }
    }
}

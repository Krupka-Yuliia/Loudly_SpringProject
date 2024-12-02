package com.loudlyapp.web_controllers;

import com.loudlyapp.artist.Artist;
import com.loudlyapp.artist.ArtistDTO;
import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.playlist.PlayListService;
import com.loudlyapp.playlist.PlaylistDTO;
import com.loudlyapp.song.SongDTO;
import com.loudlyapp.song.SongService;
import com.loudlyapp.user.UserDTO;
import com.loudlyapp.user.UserService;
import com.loudlyapp.utils.SongNotFoundException;
import io.micrometer.common.util.StringUtils;
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
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class SongWebController {

    private final SongService songService;
    private final UserService userService;
    private final PlayListService playlistService;
    private final ArtistService artistService;

    @GetMapping("/songs/{id}")
    public String getSongPage(@AuthenticationPrincipal User principal, @PathVariable Long id, Model model) {
        Optional<SongDTO> songOptional = songService.findById(id);
        Optional<UserDTO> userOptional = userService.findByUsername(principal.getUsername());

        if (songOptional.isPresent()) {
            SongDTO song = songOptional.get();
            model.addAttribute("song", song);

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
                             @RequestParam("artistName") String artistName,
                             @RequestParam("genre") String genre,
                             @RequestParam("year") Integer year,
                             Model model) throws IOException {


        Optional<ArtistDTO> artistOptional = artistService.findByName(artistName);
        List<String> errors = new ArrayList<>();
        if (file.isEmpty()) {
            errors.add("Audio file is required.");
        }

        if (StringUtils.isBlank(title) || title.length() > 100) {
            errors.add("Invalid song title. It must be non-empty and no more than 100 characters.");
        }

        if (StringUtils.isBlank(artistName) || artistOptional.isEmpty()) {
            errors.add("Invalid artist name. ");
        }

        if (StringUtils.isBlank(genre)) {
            errors.add("Genre is required.");
        }

        int currentYear = Year.now().getValue();
        if (year == null || year < 1900 || year > currentYear) {
            errors.add("Invalid year. The year must be between 1900 and the current year.");
        }


        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "add_song_form";
        }

        SongDTO songDTO = new SongDTO();
        songDTO.setTitle(title.trim());
        songDTO.setArtistName(artistName);
        songDTO.setArtistId(artistOptional.get().getId());
        songDTO.setGenre(genre.trim());
        songDTO.setYear(year);
        songDTO.setFile(file.getBytes());

        SongDTO savedSong = songService.save(songDTO);
        return "redirect:/songs/" + savedSong.getId();
    }




    @GetMapping("/songs/{id}/play")
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

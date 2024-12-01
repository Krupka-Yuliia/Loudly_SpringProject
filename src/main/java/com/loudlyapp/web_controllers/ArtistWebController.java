package com.loudlyapp.web_controllers;

import com.loudlyapp.artist.ArtistDTO;
import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.song.SongDTO;
import com.loudlyapp.song.SongService;
import com.loudlyapp.utils.InvalidInputException;
import com.loudlyapp.utils.ResourceNotFound;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class ArtistWebController {

    private final SongService songService;
    private final ArtistService artistService;

    @GetMapping("/artists/{id}")
    public String getArtist(@PathVariable Long id, Model model) {
        Optional<ArtistDTO> artistDTO = artistService.findById(id);

        if (artistDTO.isPresent()) {
            ArtistDTO artist = artistDTO.get();
            model.addAttribute("artist", artist);

            List<SongDTO> songs = songService.findByArtistId(artist.getId());
            songs.sort(Comparator.comparing(SongDTO::getTitle));

            model.addAttribute("songs", songs);
            return "artist";
        } else {
            throw new ResourceNotFound("Artist not found with id: " + id);
        }
    }


    @GetMapping("/artists")
    public String getAllArtists(Model model) {
        try {
            List<ArtistDTO> artists = artistService.getAllArtists();
            model.addAttribute("artists", artists);
            return "artists";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load artists: " + e.getMessage());
            return "error";
        }
    }



    @GetMapping("/admin/artists/add")
    public String showArtistForm(Model model) {
        model.addAttribute("artistDTO", new ArtistDTO());
        return "add_artist_form";
    }

    @PostMapping("/admin/artists/add")
    public String addArtist(@ModelAttribute ArtistDTO artistDTO, Model model) {
        try {
            ArtistDTO artist = artistService.save(artistDTO);
            return "redirect:/artists/" + artist.getId();
        } catch (Exception e) {
            throw new InvalidInputException("Failed to add artist: " + e.getMessage());
        }
    }

}

package com.loudlyapp.song;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Service
public class SongRecommendationService {

    private final SongService songService;

    public String getDailyRecommendation() {
        List<SongDTO> songs = songService.findAll();
        Random random = new Random();
        SongDTO recommendedSong = songs.get(random.nextInt(songs.size()));
        return recommendedSong.getTitle() + " by " + recommendedSong.getArtistName();
    }
}

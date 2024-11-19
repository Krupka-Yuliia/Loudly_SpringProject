package com.loudlyapp.scheduler;

import com.loudlyapp.song.SongRecommendationService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ScheduledSongRecommendation {

    private final SongRecommendationService recommendationService;

    @Scheduled(fixedRate = 5000)
    public void recommendSongs() {
        String songRecommendation = recommendationService.getDailyRecommendation();
        System.out.println("Recommended Song: " + songRecommendation);
    }
}

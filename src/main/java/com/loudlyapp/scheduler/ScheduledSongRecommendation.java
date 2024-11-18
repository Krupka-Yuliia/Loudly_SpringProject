package com.loudlyapp.scheduler;

import com.loudlyapp.song.SongRecommendationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledSongRecommendation {

    private final SongRecommendationService recommendationService;

    public ScheduledSongRecommendation(SongRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Scheduled(fixedRate = 5000)
    public void recommendSongs() {
        String songRecommendation = recommendationService.getDailyRecommendation();

        System.out.println("Recommended Song: " + songRecommendation);
    }
}

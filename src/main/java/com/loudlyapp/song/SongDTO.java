package com.loudlyapp.song;

public record SongDTO(
        long id,
        String title,
        String format,
        int artistId,
        String artistName,
        String genre,
        int year
) {
}

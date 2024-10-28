package com.loudlyapp.playlist;

import com.loudlyapp.song.SongDTO;
import java.util.List;

public record PlaylistDTO(
        long id,
        String name,
        int userId,
        List<SongDTO> songs
) {
}

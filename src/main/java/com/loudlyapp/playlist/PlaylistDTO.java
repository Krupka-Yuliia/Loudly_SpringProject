package com.loudlyapp.playlist;

import com.loudlyapp.song.SongDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlaylistDTO {
    private long id;
    private String name;
    private int userId;
    private List<SongDTO> songs;

}

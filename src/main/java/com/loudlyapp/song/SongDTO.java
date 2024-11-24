package com.loudlyapp.song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SongDTO {
    private long id;
    private String title;
    private String format;
    private byte[] file;
    private int artistId;
    private String artistName;
    private String genre;
    private int year;
}

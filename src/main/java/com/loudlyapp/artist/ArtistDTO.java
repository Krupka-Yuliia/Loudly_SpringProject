package com.loudlyapp.artist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ArtistDTO {
    private int id;
    private String nickname;
    private String biography;

}

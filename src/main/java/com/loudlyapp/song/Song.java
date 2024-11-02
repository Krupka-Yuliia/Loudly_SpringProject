package com.loudlyapp.song;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data //getters,setters,hashcode,equals
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private byte[] file;
    private String format;
    private int artistId;
    private String genre;
    private int year;

}

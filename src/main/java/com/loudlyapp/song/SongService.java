package com.loudlyapp.song;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> findAll() {
        return songRepository.findAll();
    }


}

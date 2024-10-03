package com.loudlyapp.song;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    public List<Song> findByTitle(String title);

    public List<Song> findByGenre(String genre);

    public List<Song> findByArtistId(String artistId);

//    public Song updateSong(long id, Song song);
}

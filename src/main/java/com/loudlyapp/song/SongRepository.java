package com.loudlyapp.song;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    List<Song> findByTitle(String title);

    List<Song> findByGenre(String genre);

    List<Song> findByArtistId(String artistId);
}

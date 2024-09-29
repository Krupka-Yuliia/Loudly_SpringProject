package com.loudlyapp.repositories;

import com.loudlyapp.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SongRepository extends JpaRepository<Song, Long> {


}
package com.loudlyapp.repositories;

import com.loudlyapp.entities.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Playlist findByName(String name);

    List<Playlist> findByUserId(Long userId);

}

package com.loudlyapp.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Playlist findByName(String name);

    List<Playlist> findByUserId(Long userId);

}

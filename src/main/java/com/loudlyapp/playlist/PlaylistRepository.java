package com.loudlyapp.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {


    Optional<Playlist> findById(Long id);

    List<Playlist> findByUserId(Long userId);

}

package com.loudlyapp.repositories;

import com.loudlyapp.entities.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Playlist findByName(String name);
    Optional<Playlist> findById(Long id);
    Playlist save(Playlist playlist);
    void deleteById(Long id);
    List<Playlist> findByUserId(Long userId);

}

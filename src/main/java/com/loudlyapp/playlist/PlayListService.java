package com.loudlyapp.playlist;

import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.song.Song;
import com.loudlyapp.song.SongDTO;
import com.loudlyapp.song.SongRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PlayListService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final ArtistService artistService;

//    public List<PlaylistDTO> findAll() {
//        return playlistRepository.findAll().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

    public List<PlaylistDTO> getPlaylists(long userId) {
        return playlistRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PlaylistDTO> findById(Long id) {
        return playlistRepository.findById(id)
                .map(this::convertToDTO);
    }

    public PlaylistDTO save(PlaylistDTO playlistDTO) {
        Playlist playlist = convertToEntity(playlistDTO);
        Playlist savedPlaylist = playlistRepository.save(playlist);
        return convertToDTO(savedPlaylist);
    }

    public void deleteById(Long id) {
        playlistRepository.deleteById(id);
    }

    public PlaylistDTO update(Long id, PlaylistDTO playlistDTO) {
        return playlistRepository.findById(id)
                .map(existingPlaylist -> {
                    existingPlaylist.setName(playlistDTO.getName());
                    return convertToDTO(playlistRepository.save(existingPlaylist));
                })
                .orElseThrow(() -> new EntityNotFoundException("Playlist with id " + id + " not found"));
    }

    public List<SongDTO> getAllSongsFromPlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        return playlist.getSongs().stream()
                .map(this::convertToSongDTO)
                .collect(Collectors.toList());
    }

    public void deleteAllSongsFromPlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        playlist.clearSongs();
        playlistRepository.save(playlist);
    }

    public void deleteAll() {
        playlistRepository.deleteAll();
    }



    public PlaylistDTO addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (playlist.containsSong(song)) {
            throw new RuntimeException("Playlist already contains the song");
        }

        playlist.addSong(song);
        return convertToDTO(playlistRepository.save(playlist));
    }

    public void deleteSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (playlist.containsSong(song)) {
            playlist.removeSong(song);
            playlistRepository.save(playlist);
        } else {
            throw new RuntimeException("Playlist does not contain the song");
        }
    }

    private PlaylistDTO convertToDTO(Playlist playlist) {
        List<SongDTO> songDTOs = playlist.getSongs().stream()
                .map(this::convertToSongDTO)
                .collect(Collectors.toList());
        return new PlaylistDTO(playlist.getId(), playlist.getName(), playlist.getUserId(), songDTOs);
    }

    private Playlist convertToEntity(PlaylistDTO playlistDTO) {
        Playlist playlist = new Playlist();
        playlist.setId(playlistDTO.getId());
        playlist.setName(playlistDTO.getName());
        playlist.setUserId(playlistDTO.getUserId());
        if (playlistDTO.getSongs() != null) {
            List<Song> songs = playlistDTO.getSongs().stream()
                    .map(this::convertToSongEntity)
                    .collect(Collectors.toList());
            playlist.setSongs(songs);
        }
        return playlist;
    }

    public boolean isSongInPlaylist(Long playlistId, Long songId) {
        Optional<PlaylistDTO> playlist = findById(playlistId);

        return playlist.map(playlistDTO ->
                playlistDTO.getSongs().stream()
                        .anyMatch(song -> song.getId() == songId)
        ).orElse(false);
    }

    private SongDTO convertToSongDTO(Song song) {
        SongDTO songDTO = new SongDTO();
        songDTO.setId(song.getId());
        songDTO.setTitle(song.getTitle());
        songDTO.setArtistId(song.getArtistId());
        String artistName = artistService.getArtistNameById(song.getArtistId());
        songDTO.setArtistName(artistName);
        songDTO.setFormat(song.getFormat());
        songDTO.setGenre(song.getGenre());
        songDTO.setYear(song.getYear());
        return songDTO;
    }

    private Song convertToSongEntity(SongDTO songDTO) {
        Song song = new Song();
        song.setId(songDTO.getId());
        song.setTitle(songDTO.getTitle());
        song.setArtistId(songDTO.getArtistId());
        song.setFormat(songDTO.getFormat());
        song.setGenre(songDTO.getGenre());
        song.setYear(songDTO.getYear());
        return song;
    }
}

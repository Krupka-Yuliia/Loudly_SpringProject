package com.loudlyapp.playlists;

import com.loudlyapp.artist.ArtistDTO;
import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.playlist.PlayListService;
import com.loudlyapp.playlist.PlaylistDTO;
import com.loudlyapp.song.SongDTO;
import com.loudlyapp.song.SongService;
import com.loudlyapp.user.UserDTO;
import com.loudlyapp.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PlaylistsServiceTest {

    private static final String DEFAULT_EMAIL = "test@test.com";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String DEFAULT_ROLE = "user";

    @Autowired
    PlayListService playListService;

    @Autowired
    SongService songService;

    @Autowired
    ArtistService artistService;

    @Autowired
    UserService userService;

    @AfterEach
    void tearDown() {
        playListService.deleteAll();
    }

    @Test
    public void createPlaylist() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        userDTO.setEmail(DEFAULT_EMAIL + UUID.randomUUID());
        UserDTO savedUserDTO = userService.save(userDTO);

        assertNotNull(savedUserDTO, "User was not created");

        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setName("test");
        playlistDTO.setUserId(savedUserDTO.getId());

        PlaylistDTO savedPlaylistDTO = playListService.save(playlistDTO);

        assertNotNull(savedPlaylistDTO, "Playlist was not created");
        assertEquals(savedUserDTO.getId(), savedPlaylistDTO.getUserId());
    }

    @Test
    public void findAllPlaylists() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        userDTO.setEmail(DEFAULT_EMAIL + UUID.randomUUID());
        UserDTO savedUserDTO = userService.save(userDTO);

        createAndSavePlaylistDTO(savedUserDTO.getId());
        createAndSavePlaylistDTO(savedUserDTO.getId());

        List<PlaylistDTO> playlists = playListService.findAll();

        assertNotNull(playlists, "Playlists collection should not be null");
        assertEquals(2, playlists.size(), "There should be two playlists in the collection");
    }

    @Test
    public void findPlaylistByUserId() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        userDTO.setEmail(DEFAULT_EMAIL + UUID.randomUUID());
        UserDTO savedUserDTO = userService.save(userDTO);

        createAndSavePlaylistDTO(savedUserDTO.getId());

        List<PlaylistDTO> playlistsByUser = playListService.findPlaylistsByUserId(savedUserDTO.getId());

        assertNotNull(playlistsByUser, "Playlists collection should not be null");
    }

    @Test
    public void findPlaylistById() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        userDTO.setEmail(DEFAULT_EMAIL + UUID.randomUUID());
        UserDTO savedUserDTO = userService.save(userDTO);

        PlaylistDTO playlistDTO = createAndSavePlaylistDTO(savedUserDTO.getId());
        playListService.deleteById(playlistDTO.getId());

        Optional<PlaylistDTO> foundPlaylist = playListService.findById(playlistDTO.getId());

        assertFalse(foundPlaylist.isPresent());
    }

    @Test
    public void testDeleteAllPlaylists() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        userDTO.setEmail(DEFAULT_EMAIL + UUID.randomUUID());
        UserDTO savedUserDTO = userService.save(userDTO);

        createAndSavePlaylistDTO(savedUserDTO.getId());
        createAndSavePlaylistDTO(savedUserDTO.getId());

        playListService.deleteAll();

        List<PlaylistDTO> playlists = playListService.findAll();
        assertEquals(0, playlists.size());
    }

    @Test
    public void addSongToPlaylistTest() {
        UserDTO user = createUserDTO(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        UserDTO savedUser = userService.save(user);

        assertNotNull(savedUser, "User was not created");

        PlaylistDTO playlist = new PlaylistDTO();
        playlist.setName("test");
        playlist.setUserId(savedUser.getId());
        PlaylistDTO savedPlaylist = playListService.save(playlist);

        assertNotNull(savedPlaylist, "Playlist was not created");

        ArtistDTO artist = createArtistDTO("test", "test");
        ArtistDTO savedArtist = artistService.save(artist);

        SongDTO song = createSongDTO(savedArtist.getId(), UUID.randomUUID().toString());
        SongDTO savedSong = songService.save(song);

        playListService.addSongToPlaylist(savedPlaylist.getId(), savedSong.getId());

        Optional<PlaylistDTO> retrievedPlaylist = playListService.findById(savedPlaylist.getId());

        assertTrue(retrievedPlaylist.isPresent(), "Playlist should be found by id");
        assertEquals(1, retrievedPlaylist.get().getSongs().size(), "Playlist should contain one song");
        assertEquals(song.getTitle(), retrievedPlaylist.get().getSongs().get(0).getTitle(), "Playlist should contain the added song");
    }


    @Test
    public void deleteSongFromPlaylistTest() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        userDTO.setEmail(DEFAULT_EMAIL + UUID.randomUUID());
        UserDTO savedUserDTO = userService.save(userDTO);

        PlaylistDTO playlistDTO = createAndSavePlaylistDTO(savedUserDTO.getId());

        ArtistDTO artist = createArtistDTO("test", "test");
        ArtistDTO savedArtist = artistService.save(artist);

        SongDTO songDTO = createSongDTO(savedArtist.getId(), UUID.randomUUID().toString());
        SongDTO savedSong = songService.save(songDTO);

        playListService.addSongToPlaylist(playlistDTO.getId(), savedSong.getId());

        assertTrue(playListService.getAllSongsFromPlaylist(playlistDTO.getId()).contains(savedSong));

        playListService.deleteSongFromPlaylist(playlistDTO.getId(), savedSong.getId());

        assertFalse(playListService.getAllSongsFromPlaylist(playlistDTO.getId()).contains(savedSong));
    }

    @Test
    public void updatePlaylist() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        userDTO.setEmail(DEFAULT_EMAIL + UUID.randomUUID());
        UserDTO savedUserDTO = userService.save(userDTO);
        PlaylistDTO playlistDTO = createAndSavePlaylistDTO(savedUserDTO.getId());

        PlaylistDTO updatedPlaylistDTO = new PlaylistDTO();
        updatedPlaylistDTO.setId(playlistDTO.getId());
        updatedPlaylistDTO.setName("test2");
        updatedPlaylistDTO.setUserId(savedUserDTO.getId());

        playListService.update(playlistDTO.getId(), updatedPlaylistDTO);

        PlaylistDTO retrievedPlaylist = playListService.findById(playlistDTO.getId()).orElseThrow();

        assertNotEquals(playlistDTO.getName(), retrievedPlaylist.getName());
        assertEquals(updatedPlaylistDTO.getId(), retrievedPlaylist.getId());
        assertEquals(updatedPlaylistDTO.getUserId(), retrievedPlaylist.getUserId());
    }

    @Test
    public void deletePlaylist() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        userDTO.setEmail(DEFAULT_EMAIL + UUID.randomUUID());
        UserDTO savedUserDTO = userService.save(userDTO);
        PlaylistDTO playlistDTO = createAndSavePlaylistDTO(savedUserDTO.getId());
        playListService.deleteById(playlistDTO.getId());
        Optional<PlaylistDTO> retrievedPlaylist = playListService.findById(playlistDTO.getId());
        assertFalse(retrievedPlaylist.isPresent());
    }

    @Test
    public void getAllSongsFromPlaylistTest() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        userDTO.setEmail(DEFAULT_EMAIL + UUID.randomUUID());
        UserDTO savedUserDTO = userService.save(userDTO);
        PlaylistDTO playlistDTO = createAndSavePlaylistDTO(savedUserDTO.getId());
        ArtistDTO artist = createArtistDTO("test", "test");
        ArtistDTO savedArtist = artistService.save(artist);
        SongDTO songDTO = createSongDTO(savedArtist.getId(), UUID.randomUUID().toString());
        SongDTO savedSong = songService.save(songDTO);
        playListService.addSongToPlaylist(playlistDTO.getId(), savedSong.getId());

        List<SongDTO> songs = playListService.getAllSongsFromPlaylist(playlistDTO.getId());
        assertEquals(1, songs.size());
    }

    @Test
    public void deleteAllSongsFromPlaylistTest() {
        UserDTO userDTO = createUserDTO(UUID.randomUUID().toString());
        userDTO.setEmail(DEFAULT_EMAIL + UUID.randomUUID());
        UserDTO savedUserDTO = userService.save(userDTO);

        PlaylistDTO playlistDTO = createAndSavePlaylistDTO(savedUserDTO.getId());

        ArtistDTO artist = createArtistDTO("test", "test");
        ArtistDTO savedArtist = artistService.save(artist);

        SongDTO songDTO = createSongDTO(savedArtist.getId(), UUID.randomUUID().toString());
        SongDTO savedSongDTO = songService.save(songDTO);

        assertNotNull(savedSongDTO.getId(), "Song should be saved and have an ID");

        playListService.addSongToPlaylist(playlistDTO.getId(), savedSongDTO.getId());

        List<SongDTO> songsBeforeDeletion = playListService.getAllSongsFromPlaylist(playlistDTO.getId());
        assertEquals(1, songsBeforeDeletion.size());

        playListService.deleteAllSongsFromPlaylist(playlistDTO.getId());

        List<SongDTO> songsAfterDeletion = playListService.getAllSongsFromPlaylist(playlistDTO.getId());
        assertTrue(songsAfterDeletion.isEmpty(), "Playlist should be empty after deletion");
    }


    private UserDTO createUserDTO(String username) {
        UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setPassword(DEFAULT_PASSWORD);
        user.setEmail(DEFAULT_EMAIL);
        user.setRole(DEFAULT_ROLE);
        return user;
    }


    private PlaylistDTO createAndSavePlaylistDTO(int userId) {
        return createAndSavePlaylistDTO(UUID.randomUUID().toString(), userId);
    }

    private PlaylistDTO createAndSavePlaylistDTO(String name, int userId) {
        PlaylistDTO playlist = new PlaylistDTO();
        playlist.setName(name);
        playlist.setUserId(userId);
        return playListService.save(playlist);
    }

    private ArtistDTO createArtistDTO(String nickname, String biography) {
        ArtistDTO artist = new ArtistDTO();
        artist.setNickname(nickname);
        artist.setBiography(biography);
        return artist;
    }

    private SongDTO createSongDTO(int artistId, String title) {
        SongDTO song = new SongDTO();
        song.setArtistId(artistId);
        song.setTitle(title);
        song.setFormat("MP3");
        song.setYear(2024);
        song.setGenre("Pop");
        return song;
    }
}

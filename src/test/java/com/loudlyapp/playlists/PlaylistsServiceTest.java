package com.loudlyapp.playlists;

import com.loudlyapp.artist.Artist;
import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.playlist.PlayListService;
import com.loudlyapp.playlist.Playlist;
import com.loudlyapp.song.Song;
import com.loudlyapp.song.SongService;
import com.loudlyapp.user.User;
import com.loudlyapp.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PlaylistsServiceTest {

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
        this.playListService.deleteAll();
    }

    @Test
    public void createPlaylist() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setEmail("test@test.com");
        user.setRole("user");
        User savedUser = userService.save(user);

        assertNotNull(savedUser, "User was not created");

        Playlist playlist = new Playlist();
        playlist.setName("test");
        playlist.setUserId(savedUser.getId());
        Playlist savedPlaylist = playListService.createPlaylist(playlist);

        assertNotNull(savedPlaylist, "Playlist was not created");
        assertEquals(user.getId(), savedPlaylist.getUserId());
    }

    @Test
    public void findAllPlaylists() {
        User user = new User();
        user.setUsername("testUsername");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole("user");
        User savedUser = userService.save(user);

        Playlist playlist = new Playlist();
        playlist.setName("Test Playlist");
        playlist.setUserId(savedUser.getId());
        playListService.createPlaylist(playlist);
        Playlist playlist2 = new Playlist();
        playlist2.setName("Test Playlist");
        playlist2.setUserId(savedUser.getId());
        playListService.createPlaylist(playlist2);

        Collection<Playlist> playlists = playListService.findAll();

        assertNotNull(playlists, "Playlists collection should not be null");
        assertEquals(2, playlists.size(), "There should be one playlist in the collection");
    }

    @Test
    public void findPlaylistByUserId() {
        User user = new User();
        user.setUsername("testUsername");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole("user");
        User savedUser = userService.save(user);

        Playlist playlist = new Playlist();
        playlist.setName("Test Playlist");
        playlist.setUserId(savedUser.getId());
        playListService.createPlaylist(playlist);

        Collection<Playlist> playlistsByUser = playListService.findPlaylistsByUserId(savedUser.getId());
        assertNotNull(playlistsByUser, "Playlists collection should not be null");
    }

    @Test
    public void findPlaylistById() {
        User user = new User();
        user.setUsername("testUsername");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole("user");
        User savedUser = userService.save(user);

        Playlist playlist = new Playlist();
        playlist.setName("Test Playlist");
        playlist.setUserId(savedUser.getId());
        Playlist savedPlaylist = playListService.createPlaylist(playlist);
        assertNotNull(savedPlaylist, "Playlist was not created");

        playListService.deletePlaylist(savedPlaylist.getId());
        Optional<Playlist> foundPlaylist = playListService.findById((long) savedPlaylist.getId());
        assertFalse(foundPlaylist.isPresent());
    }

    @Test
    public void deleteAllPlaylists() {
        User user = new User();
        user.setUsername("testUsername");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole("user");
        User savedUser = userService.save(user);

        Playlist playlist = new Playlist();
        playlist.setName("Test Playlist");
        playlist.setUserId(savedUser.getId());
        playListService.createPlaylist(playlist);
        Playlist playlist2 = new Playlist();
        playlist2.setName("Test Playlist");
        playlist2.setUserId(savedUser.getId());
        playListService.createPlaylist(playlist2);

        playListService.deleteAll();

        Collection<Playlist> playlists = playListService.findAll();
        assertEquals(0, playlists.size());
    }


//    @Test
//    public void addSongToPlaylistTest() {
//        User user = new User();
//        user.setUsername("test");
//        user.setPassword("test");
//        user.setEmail("test@test.com");
//        user.setRole("user");
//        User savedUser = userService.save(user);
//
//        assertNotNull(savedUser, "User was not created");
//
//        Playlist playlist = new Playlist();
//        playlist.setName("test");
//        playlist.setUserId(savedUser.getId());
//        Playlist savedPlaylist = playListService.createPlaylist(playlist);
//
//        assertNotNull(savedPlaylist, "Playlist was not created");
//
//        Artist artist = new Artist();
//        artist.setNickname("test");
//        artist.setBiography("test");
//        Artist savedArtist = artistService.save(artist);
//
//        Song song = new Song();
//        song.setArtistId(savedArtist.getId());
//        song.setTitle("test");
//        song.setFile(null);
//        song.setFormat("MP3");
//        song.setYear(2024);
//        song.setGenre("Pop");
//
//        Song savedSong = songService.save(song);
//
//        playListService.addSongToPlaylist((long) savedPlaylist.getId(), (long) savedSong.getId());
//        assertTrue(savedPlaylist.getSongs().contains(savedSong), "Playlist should contain the added song");
//    }



}

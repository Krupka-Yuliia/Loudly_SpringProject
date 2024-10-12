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

import java.util.*;

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
        User user = createUser(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        User savedUser = userService.save(user);

        assertNotNull(savedUser, "User was not created");

        Playlist playlist = new Playlist();
        playlist.setName("test");
        playlist.setUserId(savedUser.getId());

        Playlist savedPlaylist = playListService.save(playlist);

        assertNotNull(savedPlaylist, "Playlist was not created");
        assertEquals(savedUser.getId(), savedPlaylist.getUserId());
    }

    @Test
    public void findAllPlaylists() {
        User user = createUser(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        User savedUser = userService.save(user);

        createAndSavePlaylist(savedUser.getId());
        createAndSavePlaylist(savedUser.getId());

        Collection<Playlist> playlists = playListService.findAll();

        assertNotNull(playlists, "Playlists collection should not be null");
        assertEquals(2, playlists.size(), "There should be one playlist in the collection");
    }

    @Test
    public void findPlaylistByUserId() {
        User user = createUser(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        User savedUser = userService.save(user);

        createAndSavePlaylist(savedUser.getId());

        Collection<Playlist> playlistsByUser = playListService.findPlaylistsByUserId(savedUser.getId());

        assertNotNull(playlistsByUser, "Playlists collection should not be null");
    }

    @Test
    public void findPlaylistById() {
        User user = createUser(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        User savedUser = userService.save(user);

        Playlist playlist = createAndSavePlaylist(savedUser.getId());
        playListService.deleteById(playlist.getId());

        Optional<Playlist> foundPlaylist = playListService.findById(playlist.getId());

        assertFalse(foundPlaylist.isPresent());
    }

    @Test
    public void testDeleteAllPlaylists() {
        User user = createUser(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        User savedUser = userService.save(user);

        createAndSavePlaylist(savedUser.getId());
        createAndSavePlaylist(savedUser.getId());

        playListService.deleteAll();

        Collection<Playlist> playlists = playListService.findAll();
        assertEquals(0, playlists.size());
    }

    @Test
    public void addSongToPlaylistTest() {
        User user = createUser(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        User savedUser = userService.save(user);

        assertNotNull(savedUser, "User was not created");

        Playlist playlist = new Playlist();
        playlist.setName("test");
        playlist.setUserId(savedUser.getId());
        Playlist savedPlaylist = playListService.save(playlist);

        assertNotNull(savedPlaylist, "Playlist was not created");

        Artist artist = createArtist("test", "test");
        Artist savedArtist = artistService.save(artist);

        Song song = createSong(savedArtist.getId(), UUID.randomUUID().toString());
        Song savedSong = songService.save(song);

        playListService.addSongToPlaylist(savedPlaylist.getId(), savedSong.getId());

        Optional<Playlist> retrievedPlaylist = playListService.findById(playlist.getId());

        assertTrue(retrievedPlaylist.isPresent(), "Playlist should be found by id");
        assertEquals(1, retrievedPlaylist.get().getSongs().size(), "Playlist should contain one song");
        assertEquals(song.getTitle(), retrievedPlaylist.get().getSongs().get(0).getTitle(), "Playlist should contain the added song");
    }

    @Test
    public void deleteSongFromPlaylistTest() {
        User user = createUser(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        User savedUser = userService.save(user);

        Playlist playlist = createAndSavePlaylist(savedUser.getId());


        Artist artist = createArtist("test", "test");
        Artist savedArtist = artistService.save(artist);

        Song song = createSong(savedArtist.getId(), UUID.randomUUID().toString());
        Song savedSong = songService.save(song);

        playListService.addSongToPlaylist(playlist.getId(), savedSong.getId());

        assertTrue(playListService.getAllSongsFromPlaylist(playlist.getId()).contains(savedSong));

        playListService.deleteSongFromPlaylist(playlist.getId(), savedSong.getId());

        assertFalse(playListService.getAllSongsFromPlaylist(playlist.getId()).contains(savedSong));
    }

    @Test
    public void updatePlaylist() {
        User user = createUser(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        User savedUser = userService.save(user);
        Playlist playlist = createAndSavePlaylist(savedUser.getId());

        Playlist updatedPlaylist = new Playlist();
        updatedPlaylist.setId(playlist.getId());
        updatedPlaylist.setName("test2");
        updatedPlaylist.setUserId(savedUser.getId());

        playListService.update(playlist.getId(), updatedPlaylist);

        Playlist retrievedPlaylist = playListService.findById(playlist.getId()).orElseThrow();

        assertNotEquals(playlist.getName(), retrievedPlaylist.getName());
        assertEquals(updatedPlaylist.getId(), retrievedPlaylist.getId());
        assertEquals(updatedPlaylist.getUserId(), retrievedPlaylist.getUserId());
    }


    @Test
    public void deletePlaylist() {
        User user = createUser(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        User savedUser = userService.save(user);
        Playlist playlist = createAndSavePlaylist(savedUser.getId());
        playListService.deleteById(playlist.getId());
        Optional<Playlist> retrievedPlaylist = playListService.findById(playlist.getId());
        assertFalse(retrievedPlaylist.isPresent());
    }

    @Test
    public void getAllSongsFromPlaylistTest() {
        User user = createUser(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        User savedUser = userService.save(user);
        Playlist playlist = createAndSavePlaylist(savedUser.getId());
        Artist artist = createArtist("test", "test");
        Artist savedArtist = artistService.save(artist);
        Song song = createSong(savedArtist.getId(), UUID.randomUUID().toString());
        Song savedSong = songService.save(song);
        playListService.addSongToPlaylist(playlist.getId(), song.getId());

        List<Song> songs = playListService.getAllSongsFromPlaylist(playlist.getId());
        assertEquals(1, songs.size());
    }

    @Test
    public void deleteAllSongsFromPlaylistTest() {
        User user = createUser(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        User savedUser = userService.save(user);
        Playlist playlist = createAndSavePlaylist(savedUser.getId());

        Artist artist = createArtist("test", "test");
        Artist savedArtist = artistService.save(artist);

        Song song = createSong(savedArtist.getId(), UUID.randomUUID().toString());
        songService.save(song);

        playListService.addSongToPlaylist(playlist.getId(), song.getId());

        List<Song> songsBeforeDeletion = playListService.getAllSongsFromPlaylist(playlist.getId());
        assertEquals(1, songsBeforeDeletion.size());

        playListService.deleteAllSongsFromPlaylist(playlist.getId());

        List<Song> songsAfterDeletion = playListService.getAllSongsFromPlaylist(playlist.getId());
        assertTrue(songsAfterDeletion.isEmpty());
    }


    private User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(DEFAULT_PASSWORD);
        user.setEmail(DEFAULT_EMAIL);
        user.setRole(DEFAULT_ROLE);
        return user;
    }

    private Playlist createAndSavePlaylist(int userId) {
        return createAndSavePlaylist(UUID.randomUUID().toString(), userId);
    }

    private Playlist createAndSavePlaylist(String name, int userId) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setUserId(userId);
        return playListService.save(playlist);
    }

    private Artist createArtist(String nickname, String biography) {
        Artist artist = new Artist();
        artist.setNickname(nickname);
        artist.setBiography(biography);
        return artist;
    }

    private Song createSong(int artistId, String title) {
        Song song = new Song();
        song.setArtistId(artistId);
        song.setTitle(title);
        song.setFormat("MP3");
        song.setYear(2024);
        song.setGenre("Pop");
        return song;
    }
}

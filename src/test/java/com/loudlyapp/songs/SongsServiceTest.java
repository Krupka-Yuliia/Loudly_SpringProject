package com.loudlyapp.songs;

import com.loudlyapp.artist.Artist;
import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.song.Song;
import com.loudlyapp.song.SongService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SongsServiceTest {

    @Autowired
    private SongService songService;

    @Autowired
    private ArtistService artistService;

    @AfterEach
    void tearDown() {
        this.songService.deleteAll();
    }

    @Test
    public void testCreateSong() {
        Artist artist = new Artist();
        artist.setNickname("test");
        artist.setBiography("test");
        Artist savedArtist = artistService.save(artist);

        Song song = new Song();
        song.setArtistId(savedArtist.getId());
        song.setTitle("test");
        song.setFile(null);
        song.setFormat("MP3");
        song.setYear(2024);
        song.setGenre("Pop");

        Song savedSong = songService.save(song);

        assertNotNull(savedSong, "Song was not created");
        assertNotNull(savedSong.getTitle(), "Title was not created");
        assertNotNull(savedSong.getFormat(), "Format was not created");
        assertNotNull(savedSong.getGenre(), "Genre was not created");
    }

    @Test
    public void searchSongByTitleTest() {
        Artist artist = new Artist();
        artist.setNickname("test");
        artist.setBiography("test");
        Artist savedArtist = artistService.save(artist);
        assertNotNull(savedArtist, "Artist was not created");

        Song song = new Song();
        song.setArtistId(savedArtist.getId());
        song.setTitle("testSongTitle");
        song.setFile(null);
        song.setFormat("MP3");
        song.setYear(2024);
        song.setGenre("Pop");

        Song savedSong = songService.save(song);
        assertNotNull(savedSong, "Song was not created");

        List<Song> foundByTitle = songService.findByTitle(savedSong.getTitle());
        assertNotNull(foundByTitle, "No songs found with the given title");

        assertEquals(1, foundByTitle.size(), "Incorrect number of songs found");
        assertEquals(savedSong.getId(), foundByTitle.get(0).getId(), "The found song ID does not match the saved one");
        assertEquals(savedSong.getTitle(), foundByTitle.get(0).getTitle(), "The found song title does not match");
    }

    @Test
    public void updateSongTest() {
        Artist artist = new Artist();
        artist.setNickname("test");
        artist.setBiography("test");
        Artist savedArtist = artistService.save(artist);
        assertNotNull(savedArtist, "Artist was not created");

        Song song = new Song();
        song.setArtistId(savedArtist.getId());
        song.setTitle("testSongTitle");
        song.setFile(null);
        song.setFormat("MP3");
        song.setYear(2024);
        song.setGenre("Pop");
        Song savedSong = songService.save(song);
        assertNotNull(savedSong, "Song was not created");

        Song updatedSong = new Song();
        updatedSong.setArtistId(savedSong.getArtistId());
        updatedSong.setTitle("testSongTitleUpdated");
        updatedSong.setFile(savedSong.getFile());
        updatedSong.setFormat(savedSong.getFormat());
        updatedSong.setYear(savedSong.getYear());
        updatedSong.setGenre(savedSong.getGenre());

        songService.updateSong(savedSong.getId(), updatedSong);

        Optional<Song> getUpdatedSong = songService.findById(savedSong.getId());

        assertTrue(getUpdatedSong.isPresent(), "Updated song not found");
        assertEquals(savedSong.getId(), getUpdatedSong.get().getId(), "Song ID should remain the same");
        assertEquals(updatedSong.getYear(), getUpdatedSong.get().getYear(), "Year was not updated correctly");
        assertEquals(updatedSong.getTitle(), getUpdatedSong.get().getTitle(), "Title was not updated correctly");
        assertEquals(updatedSong.getGenre(), getUpdatedSong.get().getGenre(), "Genre was not updated correctly");
    }


    @Test
    public void getAllSongsTest() {
        Artist artist = new Artist();
        artist.setNickname("test");
        artist.setBiography("test");
        Artist savedArtist = artistService.save(artist);
        assertNotNull(savedArtist, "Artist was not created");

        Song song = new Song();
        song.setArtistId(savedArtist.getId());
        song.setTitle("testSongTitle");
        song.setFile(null);
        song.setFormat("MP3");
        song.setYear(2024);
        song.setGenre("Pop");
        Song savedSong = songService.save(song);
        assertNotNull(savedSong, "Song was not created");

        Collection<Song> songs = songService.findAll();
        assertNotNull(songs, "No songs found");
        assertEquals(1, songs.size());
    }

    @Test

    public void deleteSongByIdTest() {
        Artist artist = new Artist();
        artist.setNickname("test");
        artist.setBiography("test");
        Artist savedArtist = artistService.save(artist);
        assertNotNull(savedArtist, "Artist was not created");

        Song song = new Song();
        song.setArtistId(savedArtist.getId());
        song.setTitle("testSongTitle");
        song.setFile(null);
        song.setFormat("MP3");
        song.setYear(2024);
        song.setGenre("Pop");
        Song savedSong = songService.save(song);
        assertNotNull(savedSong, "Song was not created");

        songService.deleteById((long) savedSong.getId());

        Optional<Song> getDeletedSong = songService.findById(savedSong.getId());
        assertFalse(getDeletedSong.isPresent());
    }

    @Test
    public void deleteAllSongsTest() {

        Artist artist = new Artist();
        artist.setNickname("test");
        artist.setBiography("test");
        Artist savedArtist = artistService.save(artist);
        assertNotNull(savedArtist, "Artist was not created");
        Song song = new Song();
        song.setArtistId(savedArtist.getId());

        song.setTitle("testSongTitle");
        song.setFile(null);
        song.setFormat("MP3");
        song.setYear(2024);
        song.setGenre("Pop");

        Song savedSong = songService.save(song);

        assertNotNull(savedSong, "Song was not created");

        songService.deleteAll();

        Collection<Song> getDeletedSongs = songService.findAll();
        assertTrue(getDeletedSongs.isEmpty());
        assertEquals(0, getDeletedSongs.size());
    }


}

package com.loudlyapp.songs;

import com.loudlyapp.artist.Artist;
import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.song.Song;
import com.loudlyapp.song.SongService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SongsServiceTest {

    @Autowired
    private SongService songService;

    @Autowired
    private ArtistService artistService;

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



}

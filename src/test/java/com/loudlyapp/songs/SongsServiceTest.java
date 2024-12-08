package com.loudlyapp.songs;

import com.loudlyapp.AbstractMySQLContainerBaseTest;
import com.loudlyapp.artist.ArtistDTO;
import com.loudlyapp.artist.ArtistService;
import com.loudlyapp.song.SongDTO;
import com.loudlyapp.song.SongService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SongsServiceTest extends AbstractMySQLContainerBaseTest {

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
        ArtistDTO artistDTO = createArtistDTO("test", "test biography");
        ArtistDTO savedArtistDTO = artistService.save(artistDTO);

        SongDTO songDTO = createSongDTO(savedArtistDTO.getId(), UUID.randomUUID().toString());
        SongDTO savedSongDTO = songService.save(songDTO);

        assertNotNull(savedSongDTO, "Song was not created");
        assertNotNull(savedSongDTO.getTitle(), "Title was not created");
        assertNotNull(savedSongDTO.getFormat(), "Format was not created");
        assertNotNull(savedSongDTO.getGenre(), "Genre was not created");
    }

    @Test
    public void searchSongByTitleTest() {
        ArtistDTO artistDTO = createArtistDTO("test", "test biography");
        ArtistDTO savedArtistDTO = artistService.save(artistDTO);

        SongDTO songDTO = createSongDTO(savedArtistDTO.getId(), UUID.randomUUID().toString());
        SongDTO savedSongDTO = songService.save(songDTO);

        assertNotNull(savedArtistDTO, "Artist was not created");
        assertNotNull(savedSongDTO, "Song was not created");

        List<SongDTO> foundByTitle = songService.findByTitle(savedSongDTO.getTitle());
        assertNotNull(foundByTitle, "No songs found with the given title");

        assertEquals(1, foundByTitle.size(), "Incorrect number of songs found");
        assertEquals(savedSongDTO.getId(), foundByTitle.get(0).getId(), "The found song ID does not match the saved one");
        assertEquals(savedSongDTO.getTitle(), foundByTitle.get(0).getTitle(), "The found song title does not match");
    }

    @Test
    public void updateSongTest() {
        ArtistDTO artistDTO = createArtistDTO("test", "test biography");
        ArtistDTO savedArtistDTO = artistService.save(artistDTO);

        SongDTO songDTO = createSongDTO(savedArtistDTO.getId(), UUID.randomUUID().toString());
        SongDTO savedSongDTO = songService.save(songDTO);

        assertNotNull(savedArtistDTO, "Artist was not created");
        assertNotNull(savedSongDTO, "Song was not created");

        SongDTO updatedSongDTO = new SongDTO();
        updatedSongDTO.setId(savedSongDTO.getId());
        updatedSongDTO.setArtistId(savedSongDTO.getArtistId());
        updatedSongDTO.setTitle("testSongTitleUpdated");
        updatedSongDTO.setFormat(savedSongDTO.getFormat());
        updatedSongDTO.setYear(savedSongDTO.getYear());
        updatedSongDTO.setGenre(savedSongDTO.getGenre());

        songService.updateSong(savedSongDTO.getId(), updatedSongDTO);

        Optional<SongDTO> getUpdatedSongDTO = songService.findById(savedSongDTO.getId());

        assertTrue(getUpdatedSongDTO.isPresent(), "Updated song not found");
        assertEquals(savedSongDTO.getId(), getUpdatedSongDTO.get().getId(), "Song ID should remain the same");
        assertEquals(updatedSongDTO.getYear(), getUpdatedSongDTO.get().getYear(), "Year was not updated correctly");
        assertEquals(updatedSongDTO.getTitle(), getUpdatedSongDTO.get().getTitle(), "Title was not updated correctly");
        assertEquals(updatedSongDTO.getGenre(), getUpdatedSongDTO.get().getGenre(), "Genre was not updated correctly");
    }

    @Test
    public void getAllSongsTest() {
        ArtistDTO artistDTO = createArtistDTO("test", "test biography");
        ArtistDTO savedArtistDTO = artistService.save(artistDTO);

        SongDTO songDTO = createSongDTO(savedArtistDTO.getId(), UUID.randomUUID().toString());
        songService.save(songDTO);

        Collection<SongDTO> songs = songService.findAll();
        assertNotNull(songs, "No songs found");
        assertEquals(1, songs.size());
    }

    @Test
    public void deleteSongByIdTest() {
        ArtistDTO artistDTO = createArtistDTO("test", "test biography");
        ArtistDTO savedArtistDTO = artistService.save(artistDTO);

        SongDTO songDTO = createSongDTO(savedArtistDTO.getId(), UUID.randomUUID().toString());
        SongDTO savedSongDTO = songService.save(songDTO);

        songService.deleteById(savedSongDTO.getId());

        Optional<SongDTO> getDeletedSongDTO = songService.findById(savedSongDTO.getId());
        assertFalse(getDeletedSongDTO.isPresent());
    }

    @Test
    public void deleteAllSongsTest() {
        ArtistDTO artistDTO = createArtistDTO("test", "test biography");
        ArtistDTO savedArtistDTO = artistService.save(artistDTO);

        SongDTO songDTO = createSongDTO(savedArtistDTO.getId(), UUID.randomUUID().toString());
        SongDTO savedSongDTO = songService.save(songDTO);

        assertNotNull(savedSongDTO, "Song was not created");

        songService.deleteAll();

        Collection<SongDTO> getDeletedSongs = songService.findAll();
        assertTrue(getDeletedSongs.isEmpty());
        assertEquals(0, getDeletedSongs.size());
    }

    @Test
    public void getSongByIdTest() {
        ArtistDTO artistDTO = createArtistDTO("test", "test biography");
        ArtistDTO savedArtistDTO = artistService.save(artistDTO);
        SongDTO songDTO = createSongDTO(savedArtistDTO.getId(), UUID.randomUUID().toString());
        SongDTO savedSongDTO = songService.save(songDTO);
        assertNotNull(savedSongDTO, "Song was not created");

        Optional<SongDTO> getSongById = songService.findById(savedSongDTO.getId());
        assertTrue(getSongById.isPresent());
        assertEquals(savedSongDTO.getId(), getSongById.get().getId());
        assertEquals(savedSongDTO.getTitle(), getSongById.get().getTitle());
        assertEquals(savedSongDTO.getArtistId(), getSongById.get().getArtistId());
    }

    @Test
    public void getAllSongsByArtistTest() {
        ArtistDTO artistDTO = createArtistDTO("test", "test biography");
        ArtistDTO savedArtistDTO = artistService.save(artistDTO);
        SongDTO songDTO = createSongDTO(savedArtistDTO.getId(), UUID.randomUUID().toString());
        SongDTO savedSongDTO = songService.save(songDTO);
        assertNotNull(savedSongDTO, "Song was not created");

        SongDTO songDTO2 = createSongDTO(savedArtistDTO.getId(), UUID.randomUUID().toString());
        SongDTO savedSongDTO2 = songService.save(songDTO2);
        assertNotNull(savedSongDTO2, "Song was not created");

        Collection<SongDTO> getSongsByArtist = songService.findByArtistId(savedArtistDTO.getId());
        assertFalse(getSongsByArtist.isEmpty());
        assertEquals(2, getSongsByArtist.size());
    }

    @Test
    public void getAllSongsByTitleTest() {
        ArtistDTO artistDTO = createArtistDTO("test", "test biography");
        ArtistDTO savedArtistDTO = artistService.save(artistDTO);

        ArtistDTO artistDTO2 = createArtistDTO("test2", "test biography");
        ArtistDTO savedArtistDTO2 = artistService.save(artistDTO2);

        SongDTO songDTO = createSongDTO(savedArtistDTO.getId(), UUID.randomUUID().toString());
        SongDTO savedSongDTO = songService.save(songDTO);
        assertNotNull(savedSongDTO, "Song was not created");

        SongDTO songDTO2 = createSongDTO(savedArtistDTO2.getId(), savedSongDTO.getTitle());
        SongDTO savedSongDTO2 = songService.save(songDTO2);
        assertNotNull(savedSongDTO2, "Song was not created");

        Collection<SongDTO> getSongsByTitle = songService.findByTitle(savedSongDTO.getTitle());
        assertFalse(getSongsByTitle.isEmpty());
        assertEquals(2, getSongsByTitle.size());
    }

    private ArtistDTO createArtistDTO(String nickname, String biography) {
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setNickname(nickname);
        artistDTO.setBiography(biography);
        return artistDTO;
    }

    private SongDTO createSongDTO(int artistId, String title) {
        SongDTO songDTO = new SongDTO();
        songDTO.setArtistId(artistId);
        songDTO.setTitle(title);
        songDTO.setFormat("MP3");
        songDTO.setYear(2020);
        songDTO.setGenre("Pop");
        return songDTO;
    }
}

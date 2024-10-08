package com.loudlyapp.artists;

import com.loudlyapp.artist.Artist;
import com.loudlyapp.artist.ArtistService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ArtistsServiceTests {

    @Autowired
    private ArtistService artistService;


    @AfterEach
    void tearDown() {
        this.artistService.deleteAll();
    }

    @Test
    public void createAndSaveArtistTest() {
        Artist artist = createArtist("test", "test");
        Artist savedArtist = artistService.save(artist);

        assertNotNull(savedArtist, "Artist was not created");
        assertNotNull(savedArtist.getBiography(), "Bio was not created");
        assertNotNull(savedArtist.getNickname(), "Nickname was not created");
    }

    @Test
    public void getArtistByIdTest() {
        Artist artist = createArtist("test", "test");
        Artist savedArtist = artistService.save(artist);
        assertNotNull(savedArtist, "Artist was not created");
        assertNotNull(savedArtist.getBiography(), "Bio was not created");
        assertNotNull(savedArtist.getNickname(), "Nickname was not created");

        Optional<Artist> foundById = artistService.findById(savedArtist.getId());

        assertEquals(foundById.get().getId(), savedArtist.getId());
        assertEquals(foundById.get().getBiography(), savedArtist.getBiography());
        assertEquals(foundById.get().getNickname(), savedArtist.getNickname());
    }

    @Test
    public void deleteAllUsersTest() {
        Collection<Artist> artists = artistService.getAllArtists();
        assertEquals(0, artists.size());
        IntStream.range(0, 10).forEach(i -> {
            Artist artist = new Artist();
            artist.setBiography("Pop diva" + i);
            artist.setNickname("Artist" + i);
            artistService.save(artist);
        });
        Collection<Artist> savedArtists = artistService.getAllArtists();
        assertEquals(10, savedArtists.size());
        artistService.deleteAll();
        Collection<Artist> deletedArtists = artistService.getAllArtists();
        assertEquals(0, deletedArtists.size());

    }

    @Test
    public void getAllArtistsTest() {
        Artist artist = createArtist("test", "test");
        Artist savedArtist = artistService.save(artist);
        assertNotNull(savedArtist, "Artist was not created");
        Collection<Artist> artists = artistService.getAllArtists();
        assertNotNull(artists);
        assertEquals(1, artists.size());
    }

    @Test
    public void updateArtistTest() {
        Artist artist = createArtist("test", "test");
        Artist savedArtist = artistService.save(artist);
        assertNotNull(savedArtist, "Artist was not created");

        Artist updatedArtist = new Artist();
        updatedArtist.setBiography("Even better pop diva now");
        updatedArtist.setNickname(savedArtist.getNickname());

        artistService.updateArtist(savedArtist.getId(), updatedArtist);

        Optional<Artist> getUpdatedArtist = artistService.findById(savedArtist.getId());

        assertNotNull(getUpdatedArtist, "Updated artist not found");
        assertEquals(getUpdatedArtist.get().getBiography(), updatedArtist.getBiography(), "Biography was not updated");
        assertEquals(getUpdatedArtist.get().getNickname(), updatedArtist.getNickname(), "Nickname was not updated");
        assertEquals(getUpdatedArtist.get().getId(), savedArtist.getId(), "ID should remain the same");
    }

    @Test
    public void deleteArtistTest() {
        Artist artist = createArtist("test", "test");
        Artist savedArtist = artistService.save(artist);
        assertNotNull(savedArtist, "Artist was not created");

        artistService.deleteArtistById(savedArtist.getId());
        Optional<Artist> deletedArtist = artistService.findById(savedArtist.getId());

        Assertions.assertFalse(deletedArtist.isPresent());
    }

    private Artist createArtist(String nickname, String biography) {
        Artist artist = new Artist();
        artist.setNickname(nickname);
        artist.setBiography(biography);
        return artist;
    }

}

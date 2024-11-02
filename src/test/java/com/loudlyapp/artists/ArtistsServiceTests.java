package com.loudlyapp.artists;

import com.loudlyapp.artist.ArtistDTO;
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
        ArtistDTO artistDTO = createArtistDTO("test", "test");
        ArtistDTO savedArtist = artistService.save(artistDTO);

        assertNotNull(savedArtist, "Artist was not created");
        assertNotNull(savedArtist.getBiography(), "Bio was not created");
        assertNotNull(savedArtist.getNickname(), "Nickname was not created");
    }

    @Test
    public void getArtistByIdTest() {
        ArtistDTO artistDTO = createArtistDTO("test", "test");
        ArtistDTO savedArtist = artistService.save(artistDTO);
        assertNotNull(savedArtist, "Artist was not created");
        assertNotNull(savedArtist.getBiography(), "Bio was not created");
        assertNotNull(savedArtist.getNickname(), "Nickname was not created");

        Optional<ArtistDTO> foundById = artistService.findById(savedArtist.getId());

        assertEquals(foundById.get().getId(), savedArtist.getId());
        assertEquals(foundById.get().getBiography(), savedArtist.getBiography());
        assertEquals(foundById.get().getNickname(), savedArtist.getNickname());
    }

    @Test
    public void deleteAllArtistsTest() {
        Collection<ArtistDTO> artists = artistService.getAllArtists();
        assertEquals(0, artists.size());
        IntStream.range(0, 10).forEach(i -> {
            ArtistDTO artistDTO = createArtistDTO("Artist" + i, "Pop diva" + i);
            artistService.save(artistDTO);
        });
        Collection<ArtistDTO> savedArtists = artistService.getAllArtists();
        assertEquals(10, savedArtists.size());
        artistService.deleteAll();
        Collection<ArtistDTO> deletedArtists = artistService.getAllArtists();
        assertEquals(0, deletedArtists.size());
    }

    @Test
    public void getAllArtistsTest() {
        ArtistDTO artistDTO = createArtistDTO("test", "test");
        ArtistDTO savedArtist = artistService.save(artistDTO);
        assertNotNull(savedArtist, "Artist was not created");
        Collection<ArtistDTO> artists = artistService.getAllArtists();
        assertNotNull(artists);
        assertEquals(1, artists.size());
    }

    @Test
    public void updateArtistTest() {
        ArtistDTO artistDTO = createArtistDTO("test", "test");
        ArtistDTO savedArtist = artistService.save(artistDTO);
        assertNotNull(savedArtist, "Artist was not created");

        ArtistDTO updatedArtistDTO = new ArtistDTO();
        updatedArtistDTO.setBiography("Even better pop diva now");
        updatedArtistDTO.setNickname(savedArtist.getNickname());

        artistService.updateArtist(savedArtist.getId(), updatedArtistDTO);

        Optional<ArtistDTO> getUpdatedArtist = artistService.findById(savedArtist.getId());

        assertNotNull(getUpdatedArtist, "Updated artist not found");
        assertEquals(getUpdatedArtist.get().getBiography(), updatedArtistDTO.getBiography(), "Biography was not updated");
        assertEquals(getUpdatedArtist.get().getNickname(), updatedArtistDTO.getNickname(), "Nickname was not updated");
        assertEquals(getUpdatedArtist.get().getId(), savedArtist.getId(), "ID should remain the same");
    }

    @Test
    public void deleteArtistTest() {
        ArtistDTO artistDTO = createArtistDTO("test", "test");
        ArtistDTO savedArtist = artistService.save(artistDTO);
        assertNotNull(savedArtist, "Artist was not created");

        artistService.deleteArtistById(savedArtist.getId());
        Optional<ArtistDTO> deletedArtist = artistService.findById(savedArtist.getId());

        Assertions.assertFalse(deletedArtist.isPresent());
    }

    @Test
    public void searchArtistByNameTest() {
        ArtistDTO artistDTO = createArtistDTO("test", "test");
        ArtistDTO savedArtist = artistService.save(artistDTO);
        assertNotNull(savedArtist, "Artist was not created");

        Optional<ArtistDTO> foundByName = artistService.findByName(savedArtist.getNickname());

        assertNotNull(foundByName);
        assertEquals(foundByName.get().getNickname(), savedArtist.getNickname());
    }

    private ArtistDTO createArtistDTO(String nickname, String biography) {
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setNickname(nickname);
        artistDTO.setBiography(biography);
        return artistDTO;
    }
}

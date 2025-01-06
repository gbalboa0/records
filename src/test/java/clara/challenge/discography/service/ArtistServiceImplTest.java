package clara.challenge.discography.service;
import clara.challenge.discography.api.DiscogsApiClient;
import clara.challenge.discography.api.dto.ArtistDetail;
import clara.challenge.discography.api.dto.ArtistResult;
import clara.challenge.discography.api.dto.SearchResponse;
import clara.challenge.discography.dto.AlbumDto;
import clara.challenge.discography.dto.ArtistComparisonResult;
import clara.challenge.discography.dto.ArtistResponse;
import clara.challenge.discography.entity.Album;
import clara.challenge.discography.entity.Artist;
import clara.challenge.discography.entity.Genre;
import clara.challenge.discography.exceptions.InvalidArtistIdException;
import clara.challenge.discography.repository.AlbumRepository;
import clara.challenge.discography.repository.ArtistRepository;
import clara.challenge.discography.repository.GenreRepository;
import clara.challenge.discography.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArtistServiceImplTest {

    @InjectMocks
    private ArtistServiceImpl artistService;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private DiscogsApiClient discogsApiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchArtistDetailsWithAlbums_artistNotInDatabase() {
        String query = "Test Artist";
        Long artistId = 123L;
        SearchResponse mockSearchResponse = new SearchResponse();
        ArtistResult mockResult = new ArtistResult();
        mockResult.setId(artistId);
        mockSearchResponse.setResults(List.of(mockResult));

        ArtistDetail mockArtistDetail = new ArtistDetail();
        mockArtistDetail.setId(artistId);
        mockArtistDetail.setName("Test Artist");

        when(discogsApiClient.searchArtists(query)).thenReturn(mockSearchResponse);
        when(discogsApiClient.getArtistDetails(artistId)).thenReturn(mockArtistDetail);
        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        Artist result = artistService.fetchArtistDetailsWithAlbums(query);

        assertNotNull(result);
        assertEquals(artistId, result.getId());
        verify(discogsApiClient, times(1)).searchArtists(query);
        verify(discogsApiClient, times(1)).getArtistDetails(artistId);
        verify(artistRepository, times(1)).save(any(Artist.class));
    }

    @Test
    void compareArtists_validArtists_sortedByNumberOfReleases() {
        List<Long> artistIds = List.of(1L, 2L);
        List<ArtistComparisonResult> mockResults = List.of(
                new ArtistComparisonResult(1L, "Artist A", 5L, 10L),
                new ArtistComparisonResult(2L, "Artist B", 3L, 5L)
        );

        when(artistRepository.compareArtists(artistIds)).thenReturn(mockResults);
        when(genreRepository.findGenresByArtistId(anyLong())).thenReturn(List.of("Rock", "Pop"));

        List<ArtistComparisonResult> results = artistService.compareArtists(artistIds, "number_of_releases");

        assertEquals(2, results.size());
        assertEquals("Artist A", results.get(0).getArtistName());
        assertEquals("Artist B", results.get(1).getArtistName());
        verify(artistRepository, times(1)).compareArtists(artistIds);
        verify(genreRepository, times(2)).findGenresByArtistId(anyLong());
    }

    @Test
    void getArtist_validId() {
        Long artistId = 1L;
        Artist mockArtist = new Artist();
        mockArtist.setId(artistId);
        mockArtist.setName("Test Artist");

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(mockArtist));

        ArtistResponse result = artistService.getArtist(artistId);

        assertNotNull(result);
        assertEquals("Test Artist", result.getNames());
        verify(artistRepository, times(1)).findById(artistId);
    }

    @Test
    void getArtist_invalidId() {
        Long artistId = 1L;

        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        assertThrows(InvalidArtistIdException.class, () -> artistService.getArtist(artistId));
        verify(artistRepository, times(1)).findById(artistId);
    }

    @Test
    void fetchArtistDetailsWithAlbums_noSearchResults() {
        when(discogsApiClient.searchArtists("NonExistentArtist"))
                .thenReturn(new SearchResponse());

        assertThrows(InvalidArtistIdException.class, () -> artistService.fetchArtistDetailsWithAlbums("NonExistentArtist"));
        verify(discogsApiClient, times(1)).searchArtists("NonExistentArtist");
    }

    @Test
    void mapAndSaveAlbums_withExistingGenres() {
        // Mock API response
        Artist mockArtist = new Artist();
        mockArtist.setId(1L);
        mockArtist.setName("Test Artist");

        AlbumDto albumDto = new AlbumDto();
        albumDto.setId(101L);
        albumDto.setTitle("Test Album");
        albumDto.setCountry("US");
        albumDto.setYear(2000L);
        albumDto.setGenre(List.of("Rock", "Pop"));

        when(discogsApiClient.getAlbumsForArtist("Test Artist")).thenReturn(List.of(albumDto));
        when(genreRepository.findByName("Rock")).thenReturn(Optional.of(new Genre(1L, "Rock")));
        when(genreRepository.findByName("Pop")).thenReturn(Optional.of(new Genre(2L, "Pop")));

        // Act
        ReflectionTestUtils.invokeMethod(artistService, "mapAndSaveAlbums", mockArtist);

        // Assert
        verify(albumRepository, times(1)).save(any(Album.class));
        verify(genreRepository, times(2)).findByName(anyString());
    }

    @Test
    void compareArtists_withGenres() {
        // Mock repository responses
        List<Long> artistIds = List.of(1L, 2L);
        List<ArtistComparisonResult> mockResults = List.of(
                new ArtistComparisonResult(1L, "Artist A", 5L, 10L),
                new ArtistComparisonResult(2L, "Artist B", 3L, 5L)
        );

        when(artistRepository.compareArtists(artistIds)).thenReturn(mockResults);
        when(genreRepository.findGenresByArtistId(1L)).thenReturn(List.of("Rock", "Pop"));
        when(genreRepository.findGenresByArtistId(2L)).thenReturn(List.of("Jazz"));

        // Act
        List<ArtistComparisonResult> results = artistService.compareArtists(artistIds, "active_years");

        // Assert
        assertEquals(2, results.size());
        assertEquals(List.of("Rock", "Pop"), results.get(0).getGenres());
        assertEquals(List.of("Jazz"), results.get(1).getGenres());
        verify(artistRepository, times(1)).compareArtists(artistIds);
        verify(genreRepository, times(2)).findGenresByArtistId(anyLong());
    }
}


package clara.challenge.discography.service;

import clara.challenge.discography.api.DiscogsApiClient;
import clara.challenge.discography.api.dto.ArtistDetail;
import clara.challenge.discography.api.dto.ArtistResult;
import clara.challenge.discography.dto.AlbumDto;
import clara.challenge.discography.dto.ArtistComparisonResult;
import clara.challenge.discography.api.dto.SearchResponse;
import clara.challenge.discography.dto.ArtistResponse;
import clara.challenge.discography.entity.Album;
import clara.challenge.discography.entity.Artist;
import clara.challenge.discography.entity.Genre;
import clara.challenge.discography.entity.Member;
import clara.challenge.discography.enums.SortColumn;
import clara.challenge.discography.exceptions.InvalidArtistIdException;
import clara.challenge.discography.repository.AlbumRepository;
import clara.challenge.discography.repository.ArtistRepository;
import clara.challenge.discography.repository.GenreRepository;
import clara.challenge.discography.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final MemberRepository memberRepository;
    private final DiscogsApiClient discogsApiClient;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;

    @Transactional
    public Artist fetchArtistDetailsWithAlbums(String query) {
        SearchResponse searchResponse = discogsApiClient.searchArtists(query);
        if (searchResponse == null || searchResponse.getResults() == null || searchResponse.getResults().isEmpty()) {
            throw new InvalidArtistIdException("No artists found for query: " + query);
        }

        ArtistResult firstResult = searchResponse.getResults().getFirst();
        Long artistId = firstResult.getId();

        return artistRepository.findById(artistId)
                .orElseGet(() -> createAndSaveArtist(artistId));
    }

    private Artist createAndSaveArtist(Long artistId) {
        ArtistDetail artistDetail = discogsApiClient.getArtistDetails(artistId);
        Artist artist = mapArtistDetailToArtist(artistDetail);
        artistRepository.save(artist);
        mapAndSaveMembers(artistDetail, artist);
        mapAndSaveAlbums(artist);
        return artist;
    }

    private Artist mapArtistDetailToArtist(ArtistDetail artistDetail) {
        Artist artist = new Artist();
        artist.setId(artistDetail.getId());
        artist.setName(artistDetail.getName());
        artist.setDescription(artistDetail.getProfile());
        return artist;
    }

    private void mapAndSaveMembers(ArtistDetail artistDetail, Artist artist) {
        if (artistDetail.getMembers() != null) {
            for (ArtistDetail.Member memberDto : artistDetail.getMembers()) {
                Member member = new Member();
                member.setId(memberDto.getId());
                member.setName(memberDto.getName());
                member.setActive(memberDto.isActive());
                member.setArtist(artist);
                memberRepository.save(member);
                artist.getMembers().add(member);
            }
        }
    }

    private void mapAndSaveAlbums(Artist artist) {
        List<AlbumDto> albums = discogsApiClient.getAlbumsForArtist(artist.getName());

        for (AlbumDto albumDto : albums) {
            if (!albumRepository.existsById(albumDto.getId())) {
                Album album = new Album();
                album.setId(albumDto.getId());
                album.setTitle(albumDto.getTitle());
                album.setCountry(albumDto.getCountry());
                album.setYear(albumDto.getYear());
                album.setArtist(artist);

                Set<Genre> genres = mapGenres(albumDto.getGenre());
                album.setGenres(genres);

                albumRepository.save(album);
                artist.getAlbums().add(album);
            }
        }
    }

    private Set<Genre> mapGenres(List<String> genreNames) {
        Set<Genre> genres = new HashSet<>();
        for (String genreName : genreNames) {
            Genre genre = genreRepository.findByName(genreName)
                    .orElseGet(() -> {
                        Genre newGenre = new Genre();
                        newGenre.setName(genreName);
                        return genreRepository.save(newGenre); // Save and return the new genre
                    });
            genres.add(genre);
        }
        return genres;
    }

    public List<ArtistComparisonResult> compareArtists(List<Long> artistIds, String sortColumn) {
        List<ArtistComparisonResult> results = artistRepository.compareArtists(artistIds);

        results.forEach(result -> {
            List<String> genres = genreRepository.findGenresByArtistId(result.getArtistId());
            result.setGenres(genres);
        });

        // Get the comparator from the SortColumn enum
        SortColumn sortCriteria = SortColumn.fromColumnName(sortColumn);
        Comparator<ArtistComparisonResult> comparator = sortCriteria.getComparator();

        return results.stream()
                .sorted(comparator)
                .toList();
    }

    public ArtistResponse getArtist(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new InvalidArtistIdException("No artist found with id: " + id));
        return ArtistResponse.fromEntity(artist);
    }
}

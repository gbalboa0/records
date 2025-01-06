package clara.challenge.discography.service;

import clara.challenge.discography.dto.ArtistComparisonResult;
import clara.challenge.discography.dto.ArtistResponse;
import clara.challenge.discography.entity.Artist;

import java.util.List;

public interface ArtistService {
    Artist fetchArtistDetailsWithAlbums(String query);
    List<ArtistComparisonResult> compareArtists(List<Long> artistIds, String sortColumn);
    ArtistResponse getArtist(Long id);
}

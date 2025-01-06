package clara.challenge.discography.api;

import clara.challenge.discography.api.dto.ArtistDetail;
import clara.challenge.discography.api.dto.SearchResponse;
import clara.challenge.discography.dto.AlbumDto;

import java.util.List;

public interface RecordsApiClient {
    SearchResponse searchArtists(String query);

    ArtistDetail getArtistDetails(Long artistId);

    List<AlbumDto> getAlbumsForArtist(String artist);
}

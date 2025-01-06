package clara.challenge.discography.api;

import clara.challenge.discography.api.dto.DiscogsAlbumsResponse;
import clara.challenge.discography.api.dto.ArtistDetail;
import clara.challenge.discography.api.dto.SearchResponse;
import clara.challenge.discography.dto.AlbumDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscogsApiClient implements RecordsApiClient {

    private final WebClient webClient;

    public SearchResponse searchArtists(String query) {
        // Only get the first result here
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/database/search")
                        .queryParam("query", query)
                        .queryParam("type", "artist")
                        .queryParam("per_page", 1)
                        .build())
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .block();
    }

    public ArtistDetail getArtistDetails(Long artistId) {
        return webClient.get()
                .uri("/artists/{artistId}", artistId)
                .retrieve()
                .bodyToMono(ArtistDetail.class)
                .block();
    }

    public List<AlbumDto> getAlbumsForArtist(String artist) {
        try {
            DiscogsAlbumsResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/database/search")
                            .queryParam("artist", artist)
                            .queryParam("type", "master")
                            .queryParam("format", "album")
                            .queryParam("sort", "year")
                            .queryParam("sort_order", "desc")
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            Mono.error(new RuntimeException("Failed to fetch albums for artist: " + artist)))
                    .bodyToMono(DiscogsAlbumsResponse.class)
                    .block();

            return response != null ? response.getResults() : List.of();
        } catch (WebClientException e) {
            throw new RuntimeException("Error connecting to external API: " + e.getMessage(), e);
        }
    }
}

package clara.challenge.discography.controller;

import clara.challenge.discography.dto.ArtistComparisonResult;
import clara.challenge.discography.dto.ArtistResponse;
import clara.challenge.discography.entity.Artist;
import clara.challenge.discography.service.ArtistServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
@Tag(name = "Artists", description = "Endpoints for managing artists and their discographies")
public class ArtistController {

    private final ArtistServiceImpl artistServiceImpl;

    @GetMapping("/search")
    public ResponseEntity<ArtistResponse> searchAndFetchArtistDetails(
            @RequestParam
            @NotBlank(message = "Query must not be blank")
            @Size(min = 2, max = 50, message = "Query must be between 2 and 50 characters") String query) {

        Artist artist = artistServiceImpl.fetchArtistDetailsWithAlbums(query);
        ArtistResponse response = ArtistResponse.fromEntity(artist);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/compare")
    public ResponseEntity<List<ArtistComparisonResult>> compareArtists(
            @RequestParam List<Long> artistIds,
            @RequestParam String sortColumn) {
        List<ArtistComparisonResult> response = artistServiceImpl.compareArtists(artistIds, sortColumn);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponse> getArtist(@PathVariable Long id) {
        return ResponseEntity.ok(artistServiceImpl.getArtist(id));
    }
}


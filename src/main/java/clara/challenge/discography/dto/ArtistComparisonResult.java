package clara.challenge.discography.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistComparisonResult {
    private Long artistId;
    private String artistName;
    private Long numberOfReleases;
    private Long activeYears;
    private List<String> genres;

    public ArtistComparisonResult(Long artistId, String artistName, Long numberOfReleases, Long activeYears) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.numberOfReleases = numberOfReleases;
        this.activeYears = activeYears;
        this.genres = List.of();
    }
}

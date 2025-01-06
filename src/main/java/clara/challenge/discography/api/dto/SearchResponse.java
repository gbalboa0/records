package clara.challenge.discography.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchResponse {
    private List<ArtistResult> results;
}


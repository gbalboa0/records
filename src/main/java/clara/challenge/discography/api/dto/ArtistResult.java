package clara.challenge.discography.api.dto;

import lombok.Data;

@Data
public class ArtistResult {
    private Long id;
    private String type;
    private String title;
    private String thumb;
    private String cover_image;
    private String resource_url;
}


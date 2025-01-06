package clara.challenge.discography.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class AlbumDto {
    private Long id;
    private String title;
    private String country;
    private Long year;
    private List<String> genre;
}


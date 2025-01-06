package clara.challenge.discography.api.dto;

import clara.challenge.discography.dto.AlbumDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DiscogsAlbumsResponse extends DiscogsApiResponse{
    private List<AlbumDto> results;
}



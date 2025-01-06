package clara.challenge.discography.dto;

import clara.challenge.discography.entity.Album;
import clara.challenge.discography.entity.Artist;
import clara.challenge.discography.entity.Genre;
import clara.challenge.discography.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Data
public class ArtistResponse {
    private Long id;
    private String names;
    private String description;
    private List<MemberResponse> members;
    private List<AlbumResponse> albums;

    public static ArtistResponse fromEntity(Artist artist) {
        ArtistResponse response = new ArtistResponse();
        response.setId(artist.getId());
        response.setNames(artist.getName());
        response.setDescription(artist.getDescription());
        response.setMembers(MemberResponse.fromEntity(artist.getMembers()));
        response.setAlbums(AlbumResponse.fromEntity(artist.getAlbums()));
        return response;
    }

    @Data
    @AllArgsConstructor
    public static class MemberResponse {
        private Long id;
        private String name;
        private boolean active;

        public static List<MemberResponse> fromEntity(List<Member> members) {
            return members.stream()
                    .filter(Member::isActive)
                    .map(member -> new MemberResponse(member.getId(), member.getName(), true))
                    .toList();
        }
    }

    @Data
    @AllArgsConstructor
    public static class AlbumResponse {
        private Long id;
        private String title;
        private Long year;
        private List<String> genres;

        public static List<AlbumResponse> fromEntity(Set<Album> albums) {
            return albums.stream()
                    .sorted(Comparator.comparing(Album::getYear).reversed())
                    .map(album -> new AlbumResponse(album.getId(), album.getTitle(), album.getYear(), album.getGenres().stream().map(Genre::getName).toList()))
                    .toList();
        }
    }
}



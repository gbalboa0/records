package clara.challenge.discography.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArtistDetail {
    private Long id;
    private String name;
    private String profile;
    private String resource_url;
    private List<Image> images;
    private List<Member> members;

    @Data
    public static class Image {
        private String type;
        private String uri;
        private String uri150;
        private int width;
        private int height;
    }

    @Data
    public static class Member {
        private Long id;
        private String name;
        private String resource_url;
        private boolean active;
    }
}


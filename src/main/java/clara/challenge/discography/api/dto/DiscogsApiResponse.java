package clara.challenge.discography.api.dto;

import lombok.Data;

public abstract class DiscogsApiResponse {
    private Pagination pagination;

    @Data
    public static class Pagination {
        private int page;
        private int pages;
        private int per_page;
        private int items;
    }
}

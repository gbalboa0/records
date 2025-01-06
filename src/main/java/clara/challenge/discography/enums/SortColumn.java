package clara.challenge.discography.enums;

import clara.challenge.discography.dto.ArtistComparisonResult;

import java.util.Comparator;

public enum SortColumn {
    NUMBER_OF_RELEASES("number_of_releases", Comparator.comparingLong(ArtistComparisonResult::getNumberOfReleases).reversed()),
    ACTIVE_YEARS("active_years", Comparator.comparingLong(ArtistComparisonResult::getActiveYears).reversed());

    private final String columnName;
    private final Comparator<ArtistComparisonResult> comparator;

    SortColumn(String columnName, Comparator<ArtistComparisonResult> comparator) {
        this.columnName = columnName;
        this.comparator = comparator;
    }

    public String getColumnName() {
        return columnName;
    }

    public Comparator<ArtistComparisonResult> getComparator() {
        return comparator;
    }

    public static SortColumn fromColumnName(String columnName) {
        for (SortColumn sortColumn : values()) {
            if (sortColumn.columnName.equalsIgnoreCase(columnName)) {
                return sortColumn;
            }
        }
        throw new IllegalArgumentException("Invalid sort column: " + columnName);
    }
}


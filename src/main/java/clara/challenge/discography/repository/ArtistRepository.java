package clara.challenge.discography.repository;

import clara.challenge.discography.dto.ArtistComparisonResult;
import clara.challenge.discography.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    @Query(value = """
        SELECT new clara.challenge.discography.dto.ArtistComparisonResult(
            a.id,
            a.name,
            COUNT(al.id),
            COALESCE(MAX(al.year) - MIN(al.year), 0)
        )
        FROM Artist a
        LEFT JOIN a.albums al
        WHERE a.id IN :artistIds
        GROUP BY a.id, a.name
        ORDER BY COUNT(al.id) DESC
    """)
    List<ArtistComparisonResult> compareArtists(@Param("artistIds") List<Long> artistIds);
}
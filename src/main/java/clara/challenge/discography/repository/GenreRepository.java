package clara.challenge.discography.repository;

import clara.challenge.discography.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String name);

    @Query(value = """
        SELECT DISTINCT g.name
        FROM Genre g
        JOIN g.albums a
        WHERE a.artist.id = :artistId
    """)
    List<String> findGenresByArtistId(@Param("artistId") Long artistId);
}

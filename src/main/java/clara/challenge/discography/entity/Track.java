package clara.challenge.discography.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Track {
    @Id
    Long id;

    String title;
    Long duration;
    String position;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    Album album;
}

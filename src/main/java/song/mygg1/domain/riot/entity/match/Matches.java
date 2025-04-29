package song.mygg1.domain.riot.entity.match;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Matches {
    @Id
    private String matchId;

    @JoinColumn(name = "metadata_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Metadata metadata;

    @JoinColumn(name = "info_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Info info;

    public static Matches create(Metadata metadata, Info info) {
        return new Matches(metadata, info);
    }

    private Matches(Metadata metadata, Info info) {
        this.matchId = metadata.getMatchId();
        this.metadata = metadata;
        this.info = info;
    }
}

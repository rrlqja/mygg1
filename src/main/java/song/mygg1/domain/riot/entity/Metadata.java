package song.mygg1.domain.riot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.mapper.StringListConvertor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Metadata {
    @Id
    private String matchId;
    private String dataVersion;

    @Convert(converter = StringListConvertor.class)
    @Column(columnDefinition = "json")
    private List<String> participants = new ArrayList<>();

    public static Metadata create(String matchId, String dataVersion, List<String> participants) {
        return new Metadata(matchId, dataVersion, participants);
    }

    private Metadata(String matchId, String dataVersion, List<String> participants) {
        this.matchId = matchId;
        this.dataVersion = dataVersion;
        this.participants = participants;
    }
}

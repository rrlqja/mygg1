package song.mygg1.domain.riot.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Info {
    @Id
    private Long gameId;

    @OneToMany(mappedBy = "info", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "info", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> teams = new ArrayList<>();

    private String endOfGameResult;
    private String gameMode;
    private String gameName;
    private Long gameStartTimestamp;
    private String gameType;
    private String gameVersion;

    public static Info create(Long gameId, String endOfGameResult, String gameMode, String gameName, Long gameStartTimestamp, String gameType, String gameVersion) {
        return new Info(gameId, endOfGameResult, gameMode, gameName, gameStartTimestamp, gameType, gameVersion);
    }

    private Info(Long gameId, String endOfGameResult, String gameMode, String gameName, Long gameStartTimestamp, String gameType, String gameVersion) {
        this.gameId = gameId;
        this.endOfGameResult = endOfGameResult;
        this.gameMode = gameMode;
        this.gameName = gameName;
        this.gameStartTimestamp = gameStartTimestamp;
        this.gameType = gameType;
        this.gameVersion = gameVersion;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}

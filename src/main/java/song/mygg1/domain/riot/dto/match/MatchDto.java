package song.mygg1.domain.riot.dto.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.match.Matches;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto {
    private MetadataDto metadata;
    private InfoDto info;

    private boolean isWin;
    private ParticipantDto player;
    private String championImgUrl;
    private String kda;
    private String kdaAvg;
    private List<ChampionImgInfo> participantChampionImgList;

    private String imgBaseUrl = "/image/champion/";

    public Matches toEntity() {
        return Matches.create(metadata.toEntity(), info.toEntity());
    }

    public MatchDto(Matches matches, String puuid) {
        this.metadata = new MetadataDto(matches.getMetadata());
        this.info = new InfoDto(matches.getInfo());

        this.isWin = info.getParticipants().stream()
                .filter(p -> p.getPuuid().equals(puuid))
                .findFirst()
                .map(ParticipantDto::getWin)
                .orElse(false);

        this.player = info.getParticipants().stream()
                .filter(p -> p.getPuuid().equals(puuid))
                .findFirst()
                .orElse(null);

        if (player != null) {
            this.championImgUrl = imgBaseUrl + player.getChampionId();
            this.kda            = player.getKills() + " / " + player.getDeaths() + " / " + player.getAssists();
            this.kdaAvg         = String.format("%.2f", (player.getKills() + player.getAssists()) / (double) player.getDeaths());
        }

        this.participantChampionImgList = info.getParticipants().stream()
                .map(p -> new ChampionImgInfo(
                        imgBaseUrl + p.getChampionId(),
                        p.getPuuid().equals(puuid)
                ))
                .toList();
    }

    public static class ChampionImgInfo {
        private final String url;
        private final boolean isPlayer;

        public ChampionImgInfo(String url, boolean isPlayer) {
            this.url = url;
            this.isPlayer = isPlayer;
        }

        public String getUrl() {
            return url;
        }

        public boolean isPlayer() {
            return isPlayer;
        }
    }
}

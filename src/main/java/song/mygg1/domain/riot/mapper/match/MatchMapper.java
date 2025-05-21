package song.mygg1.domain.riot.mapper.match;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.match.ChampionImgInfoDto;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.dto.match.ParticipantDto;
import song.mygg1.domain.riot.entity.match.Matches;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchMapper {
    private final MetadataMapper metadataMapper;
    private final InfoMapper infoMapper;

    private final String baseImageUrl = "/image/champion/";

    public MatchDto toDto(Matches match, String puuid) {
        MatchDto dto = new MatchDto();

        dto.setMatchId(match.getMatchId());
        dto.setMetadata(metadataMapper.toDto(match.getMetadata()));
        dto.setInfo(infoMapper.toDto(match.getInfo()));

        boolean win = dto.getInfo().getParticipants().stream()
                .anyMatch(p -> p.getPuuid().equals(puuid) && p.getWin());
        dto.setWin(win);

        ParticipantDto player = dto.getInfo().getParticipants().stream()
                .filter(p -> p.getPuuid().equals(puuid))
                .findFirst().orElse(null);
        dto.setPlayer(player);

        if (player != null) {
            dto.setChampionImgUrl(baseImageUrl + player.getChampionId());
            dto.setKda(player.getKills() + " / " + player.getDeaths() + " / " + player.getAssists());
            dto.setKdaAvg(String.format("%.2f", (player.getKills() + player.getAssists()) / (double) Math.max(1, player.getDeaths())));
        }

        List<ChampionImgInfoDto> imgs = dto.getInfo().getParticipants().stream()
                .map(p -> new ChampionImgInfoDto(baseImageUrl + p.getChampionId(), p.getPuuid().equals(puuid)))
                .toList();
        dto.setParticipantChampionImgList(imgs);

        return dto;
    }

    public Matches toEntity(MatchDto dto) {
        return Matches.create(
                metadataMapper.toEntity(dto.getMetadata()),
                infoMapper.toEntity(dto.getInfo())
        );
    }
}

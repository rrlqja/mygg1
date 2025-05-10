package song.mygg1.domain.riot.mapper.match;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.match.ParticipantDto;
import song.mygg1.domain.riot.entity.match.Info;
import song.mygg1.domain.riot.entity.match.Participant;

@Component
public class ParticipantMapper {
    public ParticipantDto toDto(Participant participant) {
        ParticipantDto dto = new ParticipantDto();

        dto.setInfoId(participant.getId().getInfoId());
        dto.setParticipantId(participant.getId().getParticipantId());
        dto.setAssists(participant.getAssists());
        dto.setDeaths(participant.getDeaths());
        dto.setKills(participant.getKills());
        dto.setChampLevel(participant.getChampLevel());
        dto.setChampionId(participant.getChampionId());
        dto.setChampionName(participant.getChampionName());
        dto.setSummonerName(participant.getSummonerName());
        dto.setWin(participant.getWin());
        dto.setTeamId(participant.getTeamId());
        dto.setTotalDamageDealt(participant.getTotalDamageDealt());
        dto.setTotalDamageDealtToChampions(participant.getTotalDamageDealtToChampions());
        dto.setTotalDamageTaken(participant.getTotalDamageTaken());
        dto.setTotalHeal(participant.getTotalHeal());
        dto.setTotalHealsOnTeammates(participant.getTotalHealsOnTeammates());
        dto.setTotalMinionsKilled(participant.getTotalMinionsKilled());
        dto.setItem0(participant.getItem0()); dto.setItem1(participant.getItem1());
        dto.setItem2(participant.getItem2()); dto.setItem3(participant.getItem3());
        dto.setItem4(participant.getItem4()); dto.setItem5(participant.getItem5());
        dto.setItem6(participant.getItem6());
        dto.setPuuid(participant.getPuuid());
        dto.setRiotIdGameName(participant.getRiotIdGameName());
        dto.setRiotIdTagline(participant.getRiotIdTagline());
        dto.setSummonerId(participant.getSummonerId());
        dto.setGoldEarned(participant.getGoldEarned());
        dto.setGoldSpent(participant.getGoldSpent());
        dto.setVisionScore(participant.getVisionScore());
        dto.setVisionWardsBoughtInGame(participant.getVisionWardsBoughtInGame());
        dto.setWardsKilled(participant.getWardsKilled());
        dto.setWardsPlaced(participant.getWardsPlaced());
        dto.setSummoner1Id(participant.getSummoner1Id());
        dto.setSummoner2Id(participant.getSummoner2Id());
        String kda = participant.getKills() + " / " + participant.getDeaths() + " / " + participant.getAssists();
        dto.setKda(kda);
        dto.setKdaAvg(String.format("%.2f", (participant.getKills() + participant.getAssists()) / (double) Math.max(1, participant.getDeaths())));

        return dto;
    }

    public Participant toEntity(ParticipantDto dto, Info info) {
        return Participant.create(
                dto.getParticipantId(), dto.getAssists(), dto.getDeaths(), dto.getKills(), dto.getChampLevel(),
                dto.getChampionId(), dto.getChampionName(), dto.getSummonerName(), dto.getWin(), dto.getTeamId(),
                dto.getTotalDamageDealt(), dto.getTotalDamageDealtToChampions(), dto.getTotalDamageTaken(),
                dto.getTotalHeal(), dto.getTotalHealsOnTeammates(), dto.getTotalMinionsKilled(),
                dto.getItem0(), dto.getItem1(), dto.getItem2(), dto.getItem3(), dto.getItem4(), dto.getItem5(), dto.getItem6(),
                dto.getPuuid(), dto.getRiotIdGameName(), dto.getRiotIdTagline(), dto.getSummonerId(),
                info, dto.getGoldEarned(), dto.getGoldSpent(), dto.getVisionScore(), dto.getVisionWardsBoughtInGame(),
                dto.getWardsKilled(), dto.getWardsPlaced(), dto.getSummoner1Id(), dto.getSummoner2Id()
        );
    }
}

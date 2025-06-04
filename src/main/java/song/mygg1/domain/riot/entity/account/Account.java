package song.mygg1.domain.riot.entity.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {
    @Id
    private String puuid;

    private String gameName;
    private String tagLine;
    private LocalDateTime lastRefreshDateTime;

    public void update(String gameName, String tagLine) {
        this.gameName = gameName;
        this.tagLine = tagLine;
        this.lastRefreshDateTime = LocalDateTime.now();
    }

    public static Account create(String puuid, String gameName, String tagLine) {
        return new Account(puuid, gameName, tagLine);
    }

    private Account(String puuid, String gameName, String tagLine) {
        this.puuid = puuid;
        this.gameName = gameName;
        this.tagLine = tagLine;
        this.lastRefreshDateTime = LocalDateTime.now();
    }
}

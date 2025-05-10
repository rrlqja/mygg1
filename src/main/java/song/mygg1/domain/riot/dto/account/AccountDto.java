package song.mygg1.domain.riot.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String puuid;
    private String gameName;
    private String tagLine;
    private LocalDateTime lastRefreshDateTimeRaw;
    private String lastRefreshDateTime;
}

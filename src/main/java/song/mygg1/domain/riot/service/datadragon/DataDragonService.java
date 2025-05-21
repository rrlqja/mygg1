package song.mygg1.domain.riot.service.datadragon;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import song.mygg1.domain.common.exception.MyggException;
import song.mygg1.domain.common.exception.riot.riotapi.RiotApiException;
import song.mygg1.domain.riot.repository.datadragon.DataDragonVersionJpaRepository;
import song.mygg1.domain.riot.service.ApiService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataDragonService {
    private final DataDragonVersionJpaRepository dataDragonVersionRepository;
    private final ApiService apiService;
    private String version;
    @Value("${ddragon.path}")
    private Path ddragonPath;

    @PostConstruct
    public void init() {
        String[] dataDragonVersion = apiService.getDataDragonVersions()
                .orElseThrow(() -> new RiotApiException("No Data Dragon Version Found"));

        this.version = dataDragonVersion[0];
    }

    public String getVersion() {
        return version;
    }

    public byte[] getProfileIcon(int iconId) {
        return fetchOrPlaceholder(() ->
                apiService.getProfileIcon(version, iconId)
        );
    }

    public byte[] getChampionIcon(String championName) {
        return fetchOrPlaceholder(() ->
                apiService.getChampionIcon(version, championName)
        );
    }

    public byte[] getItemIcon(int itemId) {
        return fetchOrPlaceholder(() ->
                apiService.getItemIcon(version, itemId));
    }

    public byte[] getSpellIcon(int spellId) {
        return fetchOrPlaceholder(() ->
                apiService.getSpellIcon(version, spellId));
    }

    private byte[] fetchOrPlaceholder(Supplier<Optional<byte[]>> fetcher) {
        return fetcher.get()
                .orElseGet(() -> {
                    try {
                        return Files.readAllBytes(ddragonPath.resolve("placeteemo.png"));
                    } catch (IOException e) {
                        throw new MyggException(e.getMessage(), e);
                    }
                });
    }
}

package song.mygg1.domain.common.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import song.mygg1.domain.riot.service.champion.ChampionService;
import song.mygg1.domain.riot.service.datadragon.DataDragonService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final DataDragonService dataDragonService;
    private final ChampionService championService;
    @Value("${ddragon.path}")
    private Path ddragonPath;


    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(ddragonPath);
        createVersionDirs(dataDragonService.getVersion());
    }

    private void createVersionDirs(String version) throws IOException {
        for (String sub : List.of("profile", "champion", "item", "spell", "championspell")) {
            Files.createDirectories(ddragonPath.resolve(version).resolve(sub));
        }
        Files.createDirectories(ddragonPath.resolve("tier"));
    }

    public UrlResource getProfile(int iconId) throws IOException {
        return loadImage(
                "profile",
                iconId + ".png",
                () -> dataDragonService.getProfileIcon(iconId)
        );
    }

    public UrlResource getChampion(Long championKey) throws IOException {
        String championId = championService.getChampionId(championKey);
        log.info("championId: {}", championId);
        return loadImage(
                "champion",
                championId + ".png",
                () -> dataDragonService.getChampionIcon(championId)
        );
    }

    public UrlResource getChampionSpell(String championSpell) throws IOException {
        return loadImage(
                "championspell",
                championSpell + ".png",
                () -> dataDragonService.getChampionSpellIcon(championSpell)
        );
    }

    public UrlResource getItem(int itemId) throws IOException {
        return loadImage(
                "item",
                itemId + ".png",
                () -> dataDragonService.getItemIcon(itemId)
        );
    }

    public UrlResource getSpell(int spellId) throws IOException{
        return loadImage(
                "spell",
                spellId + ".png",
                () -> dataDragonService.getSpellIcon(spellId)
        );
    }

    public UrlResource getTier(String tierName) throws IOException {
        Path file = ddragonPath.resolve("tier").resolve("Rank_" + tierName + ".png");
        if (!Files.exists(file)) {
            return new UrlResource(ddragonPath.resolve("placeteemo.png").toUri());
        }
        return new UrlResource(file.toUri());
    }

    private UrlResource loadImage(String subDir,
                                  String fileName,
                                  Supplier<byte[]> fetcher) throws IOException {
        String version = dataDragonService.getVersion();
        createVersionDirs(version);

        Path file = ddragonPath.resolve(version)
                .resolve(subDir)
                .resolve(fileName);
        if (!Files.exists(file)) {
            byte[] img = fetcher.get();
            if (img == null || img.length == 0) {
                throw new FileNotFoundException("Image not found: " + fileName);
            }
            Files.write(file, img, StandardOpenOption.CREATE);
        }
        return new UrlResource(file.toUri());
    }
}

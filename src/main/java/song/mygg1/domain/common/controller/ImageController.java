package song.mygg1.domain.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import song.mygg1.domain.common.service.ImageService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.springframework.http.CacheControl.*;

@Slf4j
@Controller
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping(value = "/tier/{tierName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<UrlResource> getProfile(@PathVariable("tierName") String tierName) throws IOException {
        log.info("tier name: {}", tierName);
        UrlResource resource = imageService.getTier(tierName);

        return ResponseEntity.ok()
                .cacheControl(maxAge(5, MINUTES).cachePublic())
                .body(resource);
    }

    @GetMapping(value = "/profile/{iconId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<UrlResource> getProfile(@PathVariable("iconId") int iconId) throws IOException {
        UrlResource resource = imageService.getProfile(iconId);

        return ResponseEntity.ok()
                .cacheControl(maxAge(5, MINUTES).cachePublic())
                .body(resource);
    }

    @GetMapping(value = "/item/{itemId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<UrlResource> getItem(@PathVariable("itemId") int itemId) throws IOException {
        UrlResource resource = imageService.getItem(itemId);

        return ResponseEntity.ok()
                .cacheControl(maxAge(5, MINUTES).cachePublic())
                .body(resource);
    }

    @GetMapping(value = "/spell/{spellId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<UrlResource> getSpell(@PathVariable("spellId") int spellId) throws IOException {
        UrlResource resource = imageService.getSpell(spellId);

        return ResponseEntity.ok()
                .cacheControl(maxAge(5, MINUTES).cachePublic())
                .body(resource);
    }

    @GetMapping(value = "/champion/{championKey}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<UrlResource> getChampion(@PathVariable("championKey") Long championKey) throws IOException {
        UrlResource resource = imageService.getChampion(championKey);

        return ResponseEntity.ok()
                .cacheControl(maxAge(5, MINUTES).cachePublic())
                .body(resource);
    }
}

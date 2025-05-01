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
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                .body(resource);
    }

    @GetMapping(value = "/profile/{iconId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<UrlResource> getProfile(@PathVariable("iconId") int iconId) throws IOException {
        UrlResource resource = imageService.getProfile(iconId);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                .body(resource);
    }

    @GetMapping(value = "/champion/{championName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<UrlResource> getChampion(@PathVariable("championName") String championName) throws IOException {
        UrlResource resource = imageService.getChampion(championName);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                .body(resource);
    }
}

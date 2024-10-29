package song.mygg.domain.jsph.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import song.mygg.domain.common.service.RestService;
import song.mygg.domain.jsph.entity.JSPHPost;
import song.mygg.domain.jsph.exception.NotFoundException;
import song.mygg.domain.jsph.repository.JSPHPostJpaRepository;
import song.mygg.domain.jsph.util.JSPHUrl;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JSPHService {
    private final RestService restService;
    private final JSPHPostJpaRepository jsphPostJpaRepository;

    public JSPHPost getJSPHPostById(Long id) {
        Optional<JSPHPost> boardOptional = jsphPostJpaRepository.findById(id);

        if (boardOptional.isPresent()) {
            return boardOptional.get();
        }

        JSPHPost jsphPost = restService.getRest(JSPHUrl.GET_POST_BY_ID.buildUrl(Map.of("id", id)), JSPHPost.class)
                .orElseThrow(NotFoundException::new);

        return jsphPostJpaRepository.save(jsphPost);
    }
}

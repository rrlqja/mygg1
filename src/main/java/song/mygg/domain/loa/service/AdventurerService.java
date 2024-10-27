package song.mygg.domain.loa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import song.mygg.domain.common.service.RestService;
import song.mygg.domain.loa.entity.Adventurer;
import song.mygg.domain.loa.repository.AdventurerJpaRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdventurerService {
    private final AdventurerJpaRepository adventurerRepository;
    private final RestService restService;

    public Adventurer getAdventurer(String name) {
        Optional<Adventurer> adventurerOptional = adventurerRepository.findByName(name);

        if (adventurerOptional.isPresent()) {
            return adventurerOptional.get();
        }

        restService.findEntity()
        return null;
    }
}

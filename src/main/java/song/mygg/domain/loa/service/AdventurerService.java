package song.mygg.domain.loa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import song.mygg.domain.common.service.RestService;
import song.mygg.domain.loa.entity.Adventurer;
import song.mygg.domain.loa.exception.adventurer.AdventurerNotFoundException;
import song.mygg.domain.loa.repository.AdventurerJpaRepository;

import java.util.Map;
import java.util.Optional;

import static song.mygg.domain.loa.service.LoaUrl.GET_ARMORIES_PROFILE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdventurerService {
    private final AdventurerJpaRepository adventurerRepository;
    private final RestService restService;

    public Adventurer getAdventurer(String name) {
        Optional<Adventurer> optionalAdventurer = adventurerRepository.findByName(name);

        if (optionalAdventurer.isPresent()) {
            return optionalAdventurer.get();
        }

        Optional<Adventurer> optionalRest = restService.getRest(GET_ARMORIES_PROFILE.buildUrl(Map.of("characterName", name)), Adventurer.class);

        if (optionalRest.isEmpty()) {
            throw new AdventurerNotFoundException("adv 없음");
        }
        return optionalRest.get();
    }

}

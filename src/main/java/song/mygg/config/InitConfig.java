package song.mygg.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import song.mygg.domain.user.entity.User;
import song.mygg.domain.user.repository.UserJpaRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitConfig {
    private final InitData initData;

    @PostConstruct
    public void setInit() {
        initData.init();
    }

    @Slf4j
    @Component
    @RequiredArgsConstructor
    private static class InitData {
        private final UserJpaRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public void init() {
            User user = User.of("1", passwordEncoder.encode("1"));

            User user1 = userRepository.save(user);
        }
    }
}

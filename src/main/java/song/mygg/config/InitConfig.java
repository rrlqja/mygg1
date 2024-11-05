package song.mygg.config;

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


    @Slf4j
    @Component
    @RequiredArgsConstructor
    private static class InitData {
        private final UserJpaRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public void init() {
            User user = User.of("initUser", passwordEncoder.encode("1"));

            User user1 = userRepository.save(user);
        }
    }
}

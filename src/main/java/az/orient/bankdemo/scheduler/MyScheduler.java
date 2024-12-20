package az.orient.bankdemo.scheduler;

import az.orient.bankdemo.entity.UserToken;
import az.orient.bankdemo.enums.EnumAvailableStatus;
import az.orient.bankdemo.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class MyScheduler {
//    private final Environment env;

    @Value("${tokenExpiredTime}")
    private Integer tokenExpiredTime;

    private final UserTokenRepository tokenRepository;

    @Scheduled(fixedRate = 60000)
    public void start() {
        expiredToken();

    }

    private void expiredToken() {
        List<UserToken> tokenList = tokenRepository.findAllByActive(EnumAvailableStatus.ACTIVE.value);
        for (UserToken token : tokenList) {
            if ((System.currentTimeMillis() - token.getDataDate().getTime()) / 60000 >= tokenExpiredTime) {
                token.setActive(EnumAvailableStatus.DEACTIVE.value);
                tokenRepository.save(token);
            }
        }
    }
}

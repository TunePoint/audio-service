package ua.tunepoint.audio.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.tunepoint.audio.model.response.domain.User;
import ua.tunepoint.audio.service.client.UserClient;
import ua.tunepoint.audio.service.client.mapper.UserMapper;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserClient userClient;
    private final UserMapper userMapper;

    @CircuitBreaker(name = "user-service")
    public Optional<User> findUser(Long id) {
        var response = userClient.findUser(id);
        if (response == null || response.getBody() == null || response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            MDC.put("Id", Long.toString(id));
            log.warn("user was not found");

            return Optional.empty();
        }

        return Optional.ofNullable(
                userMapper.toUser(
                        response.getBody().getPayload()
                )
        );
    }
}

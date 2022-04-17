package ua.tunepoint.audio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public Optional<User> findUser(Long id) {
        var response = userClient.getProfile(id);
        if (response == null || response.getBody() == null || response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            log.warn("User with id " + id + " was not found");
            return Optional.empty();
        }

        return Optional.ofNullable(userMapper.toUser(response.getBody().getPayload()));
    }
}

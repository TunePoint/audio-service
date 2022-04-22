package ua.tunepoint.audio.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.tunepoint.audio.model.response.domain.Resource;
import ua.tunepoint.audio.service.client.ResourceClient;
import ua.tunepoint.audio.service.client.mapper.ResourceMapper;
import ua.tunepoint.web.exception.ServerException;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceClient client;
    private final ResourceMapper resourceMapper;

    @CircuitBreaker(name = "resource-service", fallbackMethod = "getResourceFallback")
    public Optional<Resource> getImage(String id) {

        var response = client.getImage(id);
        if (response == null) {
            log.error("Client returned empty ResponseEntity");

            throw new ServerException("Oops, something went wrong while getting image data");
        }

        if (response.getBody() == null || response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            log.warn("Not found response from ResourceClient");

            return Optional.empty();
        }

        return Optional.ofNullable(resourceMapper.toPayload(response.getBody().getPayload()));
    }

    @CircuitBreaker(name = "resource-service", fallbackMethod = "getResourceFallback")
    public Optional<Resource> getAudio(String id) {

        var response = client.getAudio(id);
        if (response == null) {
            log.error("Client returned empty ResponseEntity");

            throw new ServerException("Oops, something went wrong while getting audio data");
        }

        if (response.getBody() == null || response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            log.warn("Not found response from ResourceClient");

            return Optional.empty();
        }

        return Optional.ofNullable(resourceMapper.toPayload(response.getBody().getPayload()));
    }

    public Optional<Resource> getResourceFallback(String id, Throwable ex) {
        log.error("Error occurred while calling resource service", ex);
        return Optional.of(Resource.builder().id(id).build());
    }
}

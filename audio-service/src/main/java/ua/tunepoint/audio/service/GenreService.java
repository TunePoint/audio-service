package ua.tunepoint.audio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.tunepoint.audio.data.mapper.GenreMapper;
import ua.tunepoint.audio.data.repository.GenreRepository;
import ua.tunepoint.audio.model.response.payload.GenrePayload;
import ua.tunepoint.web.exception.BadRequestException;
import ua.tunepoint.web.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreMapper genreMapper;
    private final GenreRepository genreRepository;

    public Page<GenrePayload> fetchAll(Pageable pageable) {
        return genreRepository.fetchAll(pageable)
                .map(genreMapper::toPayload);
    }

    public Page<GenrePayload> fetchLike(String pattern, Pageable pageable) {
        var strippedPattern = pattern.strip();
        if (strippedPattern.length() < 3) {
            throw new BadRequestException("pattern name should be 3 symbols or greater");
        }
        return genreRepository.fetchLike(strippedPattern, pageable)
                .map(genreMapper::toPayload);
    }

    public GenrePayload fetchWithId(Long id) {
        var genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("genre with id " + id + " was not found"));
        return genreMapper.toPayload(genre);
    }
}

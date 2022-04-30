package ua.tunepoint.audio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.tunepoint.audio.data.entity.Tag;
import ua.tunepoint.audio.data.repository.TagRepository;
import ua.tunepoint.audio.model.request.TagCreateRequest;
import ua.tunepoint.audio.model.response.payload.TagPayload;
import ua.tunepoint.web.exception.BadRequestException;
import ua.tunepoint.web.exception.NotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public TagPayload create(TagCreateRequest request) {
        if (tagRepository.existsByName(request.getName())) {
            throw new BadRequestException(
                    "tag with name " + request.getName() + " already exists"
            );
        }

        var tag = tagRepository.save(
                new Tag(request.getName())
        );

        return new TagPayload(
                tag.getId(),
                tag.getName()
        );
    }

    public TagPayload findById(Long id) {
        var tag = tagRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("tag with id " + id + " was not found")
                );

        return new TagPayload(
                tag.getId(),
                tag.getName()
        );
    }

    public Set<TagPayload> findLike(String pattern) {
        return tagRepository.findLike(pattern).stream()
                .map(it -> new TagPayload(
                        it.getId(),
                        it.getName()
                ))
                .collect(Collectors.toSet());
    }
}

package ua.tunepoint.audio.data.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ua.tunepoint.audio.data.entity.audio.AudioListening;
import ua.tunepoint.audio.data.entity.audio.AudioListeningId;

public interface AudioListeningRepository extends PagingAndSortingRepository<AudioListening, AudioListeningId> {
}

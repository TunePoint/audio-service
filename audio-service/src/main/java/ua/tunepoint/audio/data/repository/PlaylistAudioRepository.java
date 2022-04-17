package ua.tunepoint.audio.data.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ua.tunepoint.audio.data.entity.playlist.PlaylistAudio;
import ua.tunepoint.audio.data.entity.playlist.PlaylistAudioIdentity;

public interface PlaylistAudioRepository extends PagingAndSortingRepository<PlaylistAudio, PlaylistAudioIdentity> {
}

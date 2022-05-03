package ua.tunepoint.audio.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.tunepoint.audio.data.entity.AccessibleEntity;
import ua.tunepoint.audio.data.entity.playlist.ManagerType;
import ua.tunepoint.audio.data.entity.playlist.Playlist;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PlaylistRepository extends PagingAndSortingRepository<Playlist, Long> {

    @Query("SELECT p.id, p.ownerId, p.isPrivate FROM Playlist p WHERE p.id = :id")
    Optional<AccessibleEntity> findAccessibleViewById(Long id);

    <T> Optional<T> findById(Long id, Class<T> projection);

    @Query("SELECT p FROM Playlist p WHERE p.ownerId = :ownerId AND (p.isPrivate = false OR p.ownerId = :userId)")
    Page<Playlist> findByOwnerIdWithAccessControl(Long ownerId, @Nullable Long userId, Pageable pageable);

    <T> Set<T> findByManagerTypeAndOwnerId(ManagerType type, Long ownerId, Class<T> projection);

    @Query("SELECT p FROM Playlist p WHERE id IN :ids")
    List<Playlist> findBulk(List<Long> ids);
}

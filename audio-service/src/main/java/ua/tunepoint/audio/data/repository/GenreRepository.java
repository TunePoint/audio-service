package ua.tunepoint.audio.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tunepoint.audio.data.entity.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}

package ua.tunepoint.audio.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tunepoint.audio.data.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}

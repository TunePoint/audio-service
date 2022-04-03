package ua.tunepoint.audio.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tunepoint.audio.data.entity.CommentStatistics;

public interface CommentStatisticsRepository extends JpaRepository<CommentStatistics, Long> {
}

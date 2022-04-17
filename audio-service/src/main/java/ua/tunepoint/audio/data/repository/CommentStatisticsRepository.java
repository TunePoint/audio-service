package ua.tunepoint.audio.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tunepoint.audio.data.entity.comment.CommentStatistics;

@Repository
public interface CommentStatisticsRepository extends JpaRepository<CommentStatistics, Long> {
}

package ua.tunepoint.audio.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tunepoint.audio.data.entity.AudioStatistics;

public interface AudioStatisticsRepository extends JpaRepository<AudioStatistics, Long> {
}

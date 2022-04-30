package ua.tunepoint.audio.data.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.tunepoint.audio.data.entity.Tag;

import java.util.Set;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {

    @Query("SELECT t FROM Tag t WHERE t.name LIKE lower(concat('%', :pattern,'%'))")
    Set<Tag> findLike(String pattern);

    boolean existsByName(String name);
}

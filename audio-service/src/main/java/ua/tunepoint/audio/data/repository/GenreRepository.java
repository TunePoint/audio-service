package ua.tunepoint.audio.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.tunepoint.audio.data.entity.Genre;

@Repository
public interface GenreRepository extends PagingAndSortingRepository<Genre, Long> {

    @Query("SELECT g FROM Genre g")
    Page<Genre> fetchAll(Pageable pageable);

    @Query("SELECT g FROM Genre g WHERE lower(g.name) LIKE lower(concat('%', :pattern,'%'))")
    Page<Genre> fetchLike(String pattern, Pageable pageable);
}

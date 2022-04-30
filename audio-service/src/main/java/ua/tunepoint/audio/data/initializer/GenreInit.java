package ua.tunepoint.audio.data.initializer;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ua.tunepoint.audio.config.properties.DomainProperties;
import ua.tunepoint.audio.data.entity.Genre;
import ua.tunepoint.audio.data.repository.GenreRepository;

import java.io.File;
import java.io.FileReader;
import java.net.URL;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenreInit implements ApplicationListener<ApplicationStartedEvent> {

    private final GenreRepository genreRepository;
    private final DomainProperties domainProperties;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationStartedEvent event) {
        if (genreRepository.count() > 0) {
            log.info("genre table is already initialized");
            return;
        }

        try (
                var csvFileReader = new FileReader(genres());
                var csvReader = new CSVReader(csvFileReader);
        ) {
            csvReader.skip(1);

            String[] row;
            while ((row = csvReader.readNext()) != null) {
                Genre genre = Genre.create(
                        readLong(row[0]),
                        readLong(row[2]),
                        row[1]
                );

                if (!genreRepository.existsById(genre.getId())) {
                    genreRepository.save(genre);
                }
            }

        } catch (Exception e) {
            log.error("Error occurred while initializing genres", e);

            throw new BeanInitializationException("Error occurred initializing genres in database", e);
        }
    }

    private File genres() {
        URL resource = getClass().getClassLoader().getResource(domainProperties.getGenreSource());
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        }

        try {
            return new File(resource.toURI());
        } catch (Exception ex)  {
            throw new BeanInitializationException("Unable to read file " + domainProperties.getGenreSource(), ex);
        }
    }

    private Long readLong(String str) {
        return StringUtils.hasText(str) ? Long.parseLong(str) : null;
    }
}

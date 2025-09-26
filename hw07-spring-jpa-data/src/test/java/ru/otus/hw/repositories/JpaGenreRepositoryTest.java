package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@DataJpaTest
@ComponentScan("ru.otus.hw.repositories")
class JpaGenreRepositoryTest {

    @Autowired
    private GenreRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать список всех жанров по заданными id")
    @Test
    void shouldReturnCorrectGenresListByIds() {
        Set<Long> ids = LongStream.range(3L, 5L).boxed().collect(Collectors.toSet());
        var actualGenres = repositoryJpa.findAllById(ids);
        assertThat(actualGenres).isNotNull().hasSize(2)
            .allMatch(s -> !s.getName().isEmpty());
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = repositoryJpa.findAll();
        assertThat(actualGenres).isNotNull().hasSize(6)
            .allMatch(s -> !s.getName().isEmpty());
    }
}
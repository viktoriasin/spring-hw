package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами ")
@JdbcTest
@Import(JdbcGenreRepository.class)
class JdbcGenreRepositoryTest {

    @Autowired
    private JdbcGenreRepository repositoryJdbc;

    @DisplayName("должен загружать список всех жанров по заданными id")
    @Test
    void shouldReturnCorrectGenresListByIds() {
        Set<Long> ids = LongStream.range(3L, 5L).boxed().collect(Collectors.toSet());
        var actualGenres = repositoryJdbc.findAllByIds(ids);
        var expectedGenres = getGenresByIds();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = repositoryJdbc.findAll();
        var expectedGenres = getGenres();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    private List<Genre> getGenres() {
        return IntStream.range(1, 7).boxed()
            .map(i -> new Genre(i, "Genre_" + i))
            .toList();
    }

    private List<Genre> getGenresByIds() {
        return IntStream.range(3, 5).boxed()
            .map(i -> new Genre(i, "Genre_" + i))
            .toList();
    }
}
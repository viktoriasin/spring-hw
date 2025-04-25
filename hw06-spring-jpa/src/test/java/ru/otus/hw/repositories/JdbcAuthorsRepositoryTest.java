package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с авторами ")
@JdbcTest
@Import(JdbcAuthorRepository.class)
class JdbcAuthorsRepositoryTest {

    @Autowired
    private JdbcAuthorRepository repositoryJdbc;

    @DisplayName("должен загружать список всех авторов по заданными id")
    @Test
    void shouldReturnCorrectAuthorsListByIds() {
        var actualAuthor = repositoryJdbc.findById(1L);
        var expectedAuthor = new Author(1L, "Author_1");

        assertThat(actualAuthor).isPresent()
            .get()
            .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = repositoryJdbc.findAll();
        var expectedAuthors = getDbAuthors();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    private List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
            .map(i -> new Author(i, "Author_" + i))
            .toList();
    }
}

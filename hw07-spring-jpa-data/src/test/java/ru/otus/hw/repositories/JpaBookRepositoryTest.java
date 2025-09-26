package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class, JpaGenreRepository.class})
class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    private static Book getNewBook() {
        return new Book(0, "Book", new Author(0, "Author"),
            new ArrayList<>(List.of(new Genre(0, "Genre"))));
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        Book persistedBook = em.persist(getNewBook());
        var actualBook = repositoryJpa.findById(persistedBook.getId());

        assertThat(actualBook).isPresent()
            .get()
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(persistedBook);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = repositoryJpa.save(getNewBook());

        assertThat(expectedBook).isNotNull()
            .matches(b -> b.getId() > 0);

        Book actualBook = em.find(Book.class, expectedBook.getId());
        assertThat(actualBook).isNotNull()
            .matches(b -> b.getId() > 0)
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = repositoryJpa.save(getNewBook());
        expectedBook.setTitle("New title");
        repositoryJpa.save(expectedBook);

        Book actualBook = em.find(Book.class, expectedBook.getId());
        assertThat(actualBook)
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        var newBook = getNewBook();
        var expectedBook = repositoryJpa.save(newBook);
        assertThat(em.find(Book.class, expectedBook.getId())).isNotNull();
        repositoryJpa.deleteById(expectedBook.getId());
        assertThat(em.find(Book.class, expectedBook.getId())).isNull();
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldNotDeleteNonExistingBook() {
        assertThatCode(() -> repositoryJpa.deleteById(111L)).doesNotThrowAnyException();
    }
}
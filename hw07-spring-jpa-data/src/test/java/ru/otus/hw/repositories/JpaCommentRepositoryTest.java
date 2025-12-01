package ru.otus.hw.repositories;


import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями ")
@DataJpaTest
@ComponentScan("ru.otus.hw.repositories")
class JpaCommentRepositoryTest {
    @Autowired
    private CommentRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCommentById() {
        val optionalActualComment = repositoryJpa.findById(1L);
        val expectedComment = em.find(Comment.class, 1L);

        assertThat(optionalActualComment).isPresent().get()
            .usingRecursiveComparison().isEqualTo(expectedComment);
    }


    @DisplayName("должен сохранять комментарий")
    @Test
    void shouldSaveComment() {
        Comment expectedComment = repositoryJpa.save(getComment());
        Comment actualComment = em.find(Comment.class, expectedComment.getId());
        assertThat(actualComment).isNotNull()
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedComment);
    }

    @DisplayName("должен обновлять комментарий")
    @Test
    void shouldUpdateComment() {
        Comment expectedComment = repositoryJpa.save(getComment());
        expectedComment.setText("New text");
        repositoryJpa.save(expectedComment);

        Comment actualComment = em.find(Comment.class, expectedComment.getId());
        assertThat(actualComment)
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() {
        Comment expectedComment = repositoryJpa.save(getComment());
        assertThat(em.find(Comment.class, expectedComment.getId())).isNotNull();
        repositoryJpa.deleteById(expectedComment.getId());
        assertThat(em.find(Comment.class, expectedComment.getId())).isNull();
    }

    private Comment getComment() {
        Book persistedBook = persistNewBookAndGet();
        return new Comment(0, "Comment", persistedBook);
    }

    private Book persistNewBookAndGet() {
        Book book = new Book(0, "Book", new Author(0, "Author"), List.of(new Genre(0, "Genre")));
        return em.persist(book);
    }
}
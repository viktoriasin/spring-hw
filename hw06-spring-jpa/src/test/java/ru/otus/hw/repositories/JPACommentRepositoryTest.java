package ru.otus.hw.repositories;


import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями ")
@DataJpaTest
@Import({JPABookRepository.class, JPACommentRepository.class})
class JPACommentRepositoryTest {
    @Autowired
    private JPACommentRepository repositoryJPA;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCommentById() {
        val optionalActualComment = repositoryJPA.findById(1L);
        val expectedComment = em.find(Comment.class, 1L);

        assertThat(optionalActualComment).isPresent().get()
            .usingRecursiveComparison().isEqualTo(expectedComment);
    }


    @DisplayName("должен сохранять комментарий")
    @Test
    void shouldSaveComment() {
        Book persistedBook = persistNewBookAndGet();
        Comment newComment = new Comment(0, "Comment", persistedBook);
        Comment savedComment = repositoryJPA.save(newComment);
        Comment comment = em.find(Comment.class, savedComment.getId());
        newComment.setId(comment.getId());
        assertThat(comment).isNotNull()
            .usingRecursiveComparison().isEqualTo(newComment);
    }

    @DisplayName("должен обновлять комментарий")
    @Test
    void shouldUpdateComment() {
        Book persistedBook = persistNewBookAndGet();
        Comment newComment = new Comment(0, "Comment", persistedBook);
        Comment savedComment = repositoryJPA.save(newComment);
        savedComment.setText("New text");
        Comment updatedComment = repositoryJPA.save(savedComment);
        Comment updatedCommentFromDB = em.find(Comment.class, updatedComment.getId());
        assertThat(updatedCommentFromDB).isNotNull();
        assertThat(updatedCommentFromDB.getText()).isEqualTo("New text");
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() {
        Book persistedBook = persistNewBookAndGet();
        Comment newComment = new Comment(0, "Comment", persistedBook);
        Comment savedComment = repositoryJPA.save(newComment);
        assertThat(em.find(Comment.class, savedComment.getId())).isNotNull();
        repositoryJPA.deleteById(savedComment.getId());
        assertThat(em.find(Comment.class, savedComment.getId())).isNull();
    }

    private Book persistNewBookAndGet() {
        Book book = new Book(0, "Book", new Author(0, "Author"), List.of(new Genre(0, "Genre")));
        return em.persist(book);
    }
}
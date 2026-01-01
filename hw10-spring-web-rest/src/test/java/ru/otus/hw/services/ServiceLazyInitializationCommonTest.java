package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.CommentDto;
import ru.otus.hw.rest.dto.GenreDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;

@DataJpaTest
@ComponentScan({"ru.otus.hw.repositories", "ru.otus.hw.services", "ru.otus.hw.converters"})
@Transactional(propagation = Propagation.NEVER)
public class ServiceLazyInitializationCommonTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BookService bookService;

    @DisplayName("Не должно возникать ошибки LazyInitialization при обращении к полям комментария " +
        "вне метода сервиса")
    @Test
    void shouldNotThrowLazyExceptionOutOfCommentService() {
        Optional<CommentDto> comment = commentService.findById(1L);
        assertThatCode(() -> comment.ifPresent(c -> c.getBook().getTitle()))
            .doesNotThrowAnyException();

        List<CommentDto> comments = commentService.findByBookId(1L);
        assertThatCode(() -> comments.stream().map(c -> c.getBook().getTitle()))
            .doesNotThrowAnyException();
    }

    @DisplayName("Не должно возникать ошибки LazyInitialization при обращении к полям книги " +
        "вне метода сервиса")
    @Test
    void shouldNotThrowLazyExceptionOutOfBookService() {
        BookDto book = bookService.findById(1L);
        assertThatCode(() -> book.getGenres().stream().map(GenreDto::getName))
            .doesNotThrowAnyException();

        List<BookDto> books = bookService.findAll();
        assertThatCode(() -> books.stream().map(b -> b.getGenres().stream().map(GenreDto::getName)))
            .doesNotThrowAnyException();
    }

    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }
}


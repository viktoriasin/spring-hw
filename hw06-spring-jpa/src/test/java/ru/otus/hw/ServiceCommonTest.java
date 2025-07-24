package ru.otus.hw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
public class ServiceCommonTest {

    @Autowired
    CommentService commentService;

    @Autowired
    BookService bookService;

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
        Optional<BookDto> book = bookService.findById(1L);
        assertThatCode(() -> book.ifPresent(b -> b.getGenres().stream().map(GenreDto::getName)))
            .doesNotThrowAnyException();

        List<BookDto> books = bookService.findAll();
        assertThatCode(() -> books.stream().map(b -> b.getGenres().stream().map(GenreDto::getName)))
            .doesNotThrowAnyException();
    }

}

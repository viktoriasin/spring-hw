

package ru.otus.hw.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ComponentScan({"ru.otus.hw.repositories", "ru.otus.hw.services", "ru.otus.hw.converters"})
//@Import({CommentRepository.class, BookRepository.class, CommentConverter.class})
@TestPropertySource(properties = {"mongock.enabled=false"})
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommentConverter commentConverter;

    private Genre genre;
    private Author author;
    private Book book;
    private Comment comment;
    private Comment comment2;
    private List<Comment> comments;

    @BeforeEach
    public void beforeAll() {
        genre = new Genre();
        genre.setName("horror");
        mongoTemplate.save(genre);

        author = new Author();
        author.setFullName("Author");
        mongoTemplate.save(author);

        book = new Book();
        book.setAuthor(author);
        book.setGenres(List.of(genre));
        book.setTitle("Title");
        mongoTemplate.save(book);

        comment = new Comment();
        comment.setText("Hello world!");
        comment.setBookId(book.getId());
        mongoTemplate.save(comment);


        comment2 = new Comment();
        comment2.setText("Hello world2!");
        comment2.setBookId(book.getId());
        mongoTemplate.save(comment2);

        comments = List.of(comment, comment2);
    }

    @AfterEach
    public void afterEach() {
        mongoTemplate.dropCollection("books");
        mongoTemplate.dropCollection("authors");
        mongoTemplate.dropCollection("genres");
        mongoTemplate.dropCollection("comments");
    }

    @Test
    void findById() {
        Optional<CommentDto> commentDto = commentService.findById(comment.getId());

        assertTrue(commentDto.isPresent());

        assertThat(commentDto.get())
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(comment);
    }

    @Test
    void findByBookId() {
        List<CommentDto> commentDtoList = commentService.findByBookId(book.getId());

        assertEquals(2, commentDtoList.size());

        List<CommentDto> commentsList = comments.stream()
            .map(commentConverter::commentToDto)
            .toList();

        assertThat(commentDtoList).containsExactlyInAnyOrderElementsOf(commentsList);
    }

    @Test
    void deleteByBookId() {
        commentService.deleteByBookId(book.getId());

        assertNull(mongoTemplate.findById(comment.getId(), Comment.class));
        assertNull(mongoTemplate.findById(comment2.getId(), Comment.class));
    }
}

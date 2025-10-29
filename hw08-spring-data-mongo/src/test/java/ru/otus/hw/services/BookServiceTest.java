package ru.otus.hw.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ComponentScan({"ru.otus.hw.repositories", "ru.otus.hw.services", "ru.otus.hw.converters"})
@TestPropertySource(properties = {"mongock.enabled=false"})
class BookServiceTest {

    @Autowired
    BookService bookService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookConverter bookConverter;

    private Genre genre;
    private Author author;
    private Book book;

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
    }

    @AfterEach
    public void afterEach() {
        mongoTemplate.dropCollection("books");
        mongoTemplate.dropCollection("authors");
        mongoTemplate.dropCollection("genres");
    }

    @Test
    void findById() {
        assertThat(mongoTemplate.getDb()).isNotNull();

        Optional<BookDto> actualBook = bookService.findById(book.getId());

        assertTrue(actualBook.isPresent());

        assertThat(actualBook.get())
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(book);
    }

    @Test
    void findByIdNonExistingBook() {
        Optional<BookDto> actualBook = bookService.findById("111");

        assertFalse(actualBook.isPresent());
    }

    @Test
    void findAll() {
        List<BookDto> allBooks = bookService.findAll();
        List<BookDto> allExpectedBook = mongoTemplate.findAll(Book.class)
            .stream()
            .map(bookConverter::bookToDto)
            .toList();

        assertThat(allBooks).containsExactlyInAnyOrderElementsOf(allExpectedBook);
    }

    @Test
    void insert() {
        BookDto newBook = bookService.insert("New book", author.getId(), Set.of(genre.getId()));

        Book expectedBook = mongoTemplate.findById(newBook.getId(), Book.class);

        assertNotNull(expectedBook);

        assertThat(newBook)
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedBook);
    }

    @Test
    void update() {
        BookDto newBook = bookService.insert("New book", author.getId(), Set.of(genre.getId()));

        Book expectedBook = mongoTemplate.findById(newBook.getId(), Book.class);

        BookDto updatedBook = bookService.update(newBook.getId(), "Updated title",
            book.getAuthor().getId(),
            book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));

        Book updatedExpectedBook = mongoTemplate.findById(newBook.getId(), Book.class);

        assertThat(updatedBook)
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(updatedExpectedBook);
    }

    @Test
    void deleteById() {
        bookService.deleteById(book.getId());

        Book bookDeleted = mongoTemplate.findById(book.getId(), Book.class);

        assertNull(bookDeleted);
    }

    @Test
    void deleteByIdThatDoesNotExist() {
        assertThatCode(() -> bookService.deleteById("111")).doesNotThrowAnyException();
    }
}
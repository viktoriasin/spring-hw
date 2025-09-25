package ru.otus.hw.services;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan({"ru.otus.hw.repositories", "ru.otus.hw.services", "ru.otus.hw.converters"})
@Transactional(Transactional.TxType.NEVER)
class BookServiceTest {

    @Autowired
    BookService bookService;

    @Autowired
    private TestEntityManager em;

    @Test
    void findById() {
        long id = 1;
        Optional<BookDto> book = bookService.findById(id);

        assertTrue(book.isPresent());

        Book expectedBook = em.find(Book.class, id);

        assertThat(book.get())
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedBook);
    }

    @Test
    void findAll() {
        List<BookDto> allBooks = bookService.findAll();
        BookConverter bookConverter = new BookConverter(new AuthorConverter(), new GenreConverter());
        List<BookDto> allExpectedBook = em.getEntityManager().createQuery("""
            select b
            from Book b
            fetch left join b.author a
            """, Book.class).getResultList().stream().map(bookConverter::bookToDto).toList();

        assertThat(allBooks).containsExactlyInAnyOrderElementsOf(allExpectedBook);


    }

    @Test
    void insert() {
        BookDto newBook = bookService.insert("New book", 1, Set.of(1L));

        Book expectedBook = em.find(Book.class, newBook.getId());

        assertNotNull(expectedBook);

        assertThat(newBook)
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedBook);
    }

    @Test
    void update() {
        BookDto newBook = bookService.insert("New book", 1, Set.of(1L));

        Book book = em.find(Book.class, newBook.getId());

        BookDto updatedBook = bookService.update(newBook.getId(), "Updated title",
            book.getAuthor().getId(),
            book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));

        Book updatedExpectedBook = em.find(Book.class, newBook.getId());

        assertThat(updatedBook)
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(updatedExpectedBook);
    }

    @Test
    void deleteById() {
        BookDto newBook = bookService.insert("New book", 1, Set.of(1L));
        Book expectedBook = em.find(Book.class, newBook.getId());

        assertNotNull(expectedBook);
        bookService.deleteById(expectedBook.getId());

        assertNull(em.find(Book.class, newBook.getId()));
    }
}
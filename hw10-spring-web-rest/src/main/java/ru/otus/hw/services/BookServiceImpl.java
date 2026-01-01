package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.exceptions.BookNotFoundException;
import ru.otus.hw.rest.exceptions.NotFoundException;

import java.util.List;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Not found"));
        return BookDto.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            throw new NotFoundException("Books");
        }
        return books.stream().map(BookDto::toDto).toList();
    }

    @Override
    @Transactional
    public BookDto insert(String title, long authorId, Set<Long> genresIds) {
        Book book = new Book(0, null, null, null);

        return BookDto.toDto(save(book, title, authorId, genresIds));
    }

    @Override
    @Transactional
    public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("There is no book with id %s to update".formatted(id)));

        return BookDto.toDto(save(book, title, authorId, genresIds));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(Book book, String title, long authorId, Set<Long> genresIds) {
        Author author = getAuthorById(authorId);
        List<Genre> genres = getGenresById(genresIds);

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenres(genres);

        return bookRepository.save(book);
    }

    private Author getAuthorById(long authorId) {
        return authorRepository.findById(authorId).orElseThrow(
            () -> new BookNotFoundException(
                "There is no author with id %s".formatted(authorId)));
    }

    private List<Genre> getGenresById(Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        List<Genre> genres = genreRepository.findAllById(genresIds);

        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new BookNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        return genres;
    }
}

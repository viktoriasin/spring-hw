package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findById(long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        return bookOptional.map(bookConverter::bookToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(bookConverter::bookToDto).toList();
    }

    @Override
    @Transactional
    public BookDto insert(String title, long authorId, Set<Long> genresIds) {
        Book book = new Book(0, null, null, null);

        return bookConverter.bookToDto(save(book, title, authorId, genresIds));
    }

    @Override
    @Transactional
    public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("There is no book with id %s to update".formatted(id)));

        return bookConverter.bookToDto(save(book, title, authorId, genresIds));
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
            () -> new EntityNotFoundException(
                "There is no author with id %s".formatted(authorId)));
    }

    private List<Genre> getGenresById(Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        List<Genre> genres = genreRepository.findAllById(genresIds);

        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        return genres;
    }
}

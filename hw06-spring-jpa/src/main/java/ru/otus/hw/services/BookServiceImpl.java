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
        return bookConverter.bookToDto(save(0, title, authorId, genresIds));
    }

    @Override
    @Transactional
    public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
        return bookConverter.bookToDto(save(id, title, authorId, genresIds));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        Book book;
        if (id == 0) {
            book = new Book(0, null, null, null);
        } else {
            book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("There is no book with id %s to update".formatted(id)));
        }

        Author newAuthor = authorRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("There is no author with id %s to update book with id %s".formatted(authorId, id)));
        List<Genre> newGenres = genreRepository.findAllByIds(genresIds);

        if (isEmpty(newGenres) || genresIds.size() != newGenres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        book.setTitle(title);
        book.setAuthor(newAuthor);
        book.setGenres(newGenres);

        return bookRepository.save(book);
    }
}

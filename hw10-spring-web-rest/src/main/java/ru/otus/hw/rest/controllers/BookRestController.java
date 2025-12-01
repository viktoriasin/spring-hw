package ru.otus.hw.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.forms.BookForm;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.exceptions.NotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookRestController {
    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;

    @GetMapping("/api/v1/books")
    public List<BookDto> listAllBookPage() {
        List<BookDto> books = bookService.findAll();

        if (books.isEmpty()) {
            throw new NotFoundException();
        }
        return books;
    }

    @GetMapping("/api/v1/books/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") long id) {
        Optional<BookDto> book = bookService.findById(id);

        if (book.isEmpty()) {
            throw new NotFoundException();
        }
        return ResponseEntity.ok(book.get());
    }

    @PutMapping("/api/v1/books/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id,
                                              @Valid @RequestBody BookForm bookForm
    ) {
        if (!id.equals(bookForm.getId())) {
            return ResponseEntity.badRequest().build();
        }

        BookDto updatedBook = bookService.update(
            id,
            bookForm.getTitle(),
            bookForm.getAuthorId(),
            bookForm.getGenresId()
        );

        return ResponseEntity.ok(updatedBook);
    }


    @DeleteMapping("/api/v1/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") long id) {
        bookService.findById(id).orElseThrow(NotFoundException::new);
        bookService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/api/v1/books")
    public ResponseEntity<?> createBook(@Valid @RequestBody BookForm bookForm) {
        bookService.insert(bookForm.getTitle(), bookForm.getAuthorId(),
            new HashSet<>(bookForm.getGenresId()));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

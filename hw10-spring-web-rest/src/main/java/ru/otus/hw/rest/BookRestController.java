package ru.otus.hw.rest;

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
//
//    @GetMapping("/edit")
//    public String editBookPage(@RequestParam("id") long id, Model model) {
//        BookDto bookDto = bookService.findById(id).orElseThrow(NotFoundException::new);
//
//        BookForm bookForm = new BookForm();
//        bookForm.setId(bookDto.getId());
//        bookForm.setTitle(bookDto.getTitle());
//        bookForm.setAuthorId(bookDto.getAuthor().getId());
//        Set<Long> genreIds = bookDto.getGenres().stream()
//            .map(GenreDto::getId)
//            .collect(Collectors.toSet());
//        bookForm.setGenresId(genreIds);
//
//        fillModel(model, genreService.findAll(), authorService.findAll(), bookForm);
//
//        return "bookEdit";
//    }
//
//    @PostMapping("/edit")
//    public String editBookPage(@Valid @ModelAttribute("bookForm") BookForm bookForm,
//                               BindingResult bindingResult,
//                               Model model) {
//        if (bindingResult.hasErrors()) {
//            fillModel(model, genreService.findAll(), authorService.findAll(), bookForm);
//
//            return "bookEdit";
//        }
//
//        bookService.update(bookForm.getId(), bookForm.getTitle(), bookForm.getAuthorId(),
//            new HashSet<>(bookForm.getGenresId()));
//        return "redirect:/";
//    }
//
//    @GetMapping("/create")
//    public String createBook(Model model) {
//        fillModel(model, genreService.findAll(), authorService.findAll(), new BookForm());
//
//        return "bookCreate";
//    }
//
//    @PostMapping("/create")
//    public String createBook(@Valid @ModelAttribute("bookForm") BookForm bookForm,
//                             BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            log.info(bindingResult.getAllErrors().toString());
//
//            fillModel(model, genreService.findAll(), authorService.findAll(), bookForm);
//            return "bookCreate";
//        }
//
//        bookService.insert(bookForm.getTitle(), bookForm.getAuthorId(),
//            new HashSet<>(bookForm.getGenresId()));
//        return "redirect:/";
//    }
//
//    @PostMapping("/delete")
//    public String deleteBook(@RequestParam("id") long id) {
//        bookService.findById(id).orElseThrow(NotFoundException::new);
//        bookService.deleteById(id);
//        return "redirect:/";
//    }
//
//
//    private void fillModel(Model model, List<GenreDto> allGenres, List<AuthorDto> allAuthors, BookForm bookForm) {
//        model.addAttribute("allGenres", allGenres);
//        model.addAttribute("allAuthors", allAuthors);
//        model.addAttribute("bookForm", bookForm);
//
//    }
}

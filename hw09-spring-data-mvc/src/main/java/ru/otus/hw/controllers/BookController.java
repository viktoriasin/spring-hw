package ru.otus.hw.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.forms.BookForm;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.HashSet;
import java.util.List;

import static ru.otus.hw.converters.GenreConverter.dtoGenresToGenresIds;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;

    @GetMapping("/")
    public String listAllBookPage(Model model) {
        List<BookDto> books = bookService.findAll();

        model.addAttribute("books", books);
        return "bookList";
    }

    @GetMapping("/edit")
    public String editBookPage(@RequestParam("id") long id, Model model) {
        BookDto bookDto = bookService.findById(id);

        BookForm bookForm = new BookForm(
            bookDto.getId(),
            bookDto.getTitle(),
            bookDto.getAuthor().getId(),
            dtoGenresToGenresIds(bookDto.getGenres())
        );
        fillModel(model, bookForm);

        return "bookEdit";
    }

    @PostMapping("/edit")
    public String editBookPage(@Valid @ModelAttribute("bookForm") BookForm bookForm,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            fillModel(model, bookForm);

            return "bookEdit";
        }

        bookService.update(bookForm.getId(), bookForm.getTitle(), bookForm.getAuthorId(),
            new HashSet<>(bookForm.getGenresId()));
        return "redirect:/";
    }

    @GetMapping("/create")
    public String createBook(Model model) {
        fillModel(model, new BookForm());

        return "bookCreate";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute("bookForm") BookForm bookForm,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info(bindingResult.getAllErrors().toString());

            fillModel(model, bookForm);
            return "bookCreate";
        }

        bookService.insert(bookForm.getTitle(), bookForm.getAuthorId(),
            new HashSet<>(bookForm.getGenresId()));
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }


    private void fillModel(Model model, BookForm bookForm) {
        model.addAttribute("allGenres", genreService.findAll());
        model.addAttribute("allAuthors", authorService.findAll());
        model.addAttribute("bookForm", bookForm);

    }
}

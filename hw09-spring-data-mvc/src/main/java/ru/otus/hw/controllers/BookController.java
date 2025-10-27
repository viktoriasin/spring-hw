package ru.otus.hw.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    public String listAllBookPage(Model model) {
        List<BookDto> books = bookService.findAll();

        model.addAttribute("books", books);
        return "bookList";
    }

    @GetMapping("/edit")
    public String editBookPage(@RequestParam("id") long id, Model model) {
        BookDto book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        return "bookEdit";
    }

    @PostMapping("/edit")
    public String saveBook(@Valid @ModelAttribute("book") BookDto book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }

        bookService.update(book.getId(), book.getTitle(), book.getAuthor().getId(),
            book.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()));
        return "redirect:/bookList";
    }

    @GetMapping("/create")
    public String createBook(Model model) {
        return "bookCreate";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute("book") BookDto book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }

        bookService.insert(book.getTitle(), book.getAuthor().getId(),
            book.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()));
        return "redirect:/bookList";
    }

    @DeleteMapping("/delete")
    public String deleteBook(@RequestParam("id") long id, Model model) {
       bookService.deleteById(id);
        return "redirect:/bookList";
    }
}

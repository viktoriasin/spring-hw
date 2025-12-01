package ru.otus.hw.page;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.forms.BookForm;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.rest.exceptions.NotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookPageController {

    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;

    @GetMapping("/")
    public String listAllBookPage() {
        return "bookList";
    }

    @GetMapping("/edit")
    public String editBookPage(@RequestParam("id") long id, Model model) {
        BookDto bookDto = bookService.findById(id).orElseThrow(NotFoundException::new);

        BookForm bookForm = new BookForm();
        bookForm.setId(bookDto.getId());
        bookForm.setTitle(bookDto.getTitle());
        bookForm.setAuthorId(bookDto.getAuthor().getId());
        Set<Long> genreIds = bookDto.getGenres().stream()
            .map(GenreDto::getId)
            .collect(Collectors.toSet());
        bookForm.setGenresId(genreIds);

        fillModel(model, genreService.findAll(), authorService.findAll(), bookForm);

        return "bookEdit";
    }

    @GetMapping("/create")
    public String createBook() {
        return "bookCreate";
    }

    private void fillModel(Model model, List<GenreDto> allGenres, List<AuthorDto> allAuthors, BookForm bookForm) {
        model.addAttribute("allGenres", allGenres);
        model.addAttribute("allAuthors", allAuthors);
        model.addAttribute("bookForm", bookForm);

    }
}

package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GenreAndAuthorPageController {
    private final GenreService genreService;
    private final AuthorService authorService;

    @GetMapping("/genres")
    public String getAllGenres(Model model) {
        List<GenreDto> allGenres = genreService.findAll();
        model.addAttribute("allGenres", allGenres);

        return "genreList";
    }

    @GetMapping("/authors")
    public String getAllAuthors(Model model) {
        List<AuthorDto> allAuthors = authorService.findAll();
        model.addAttribute("allAuthors", allAuthors);

        return "authorList";
    }
}

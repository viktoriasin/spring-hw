package ru.otus.hw.rest.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.rest.exceptions.NotFoundException;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GenresRestControllers {
    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    public List<GenreDto> getGenres() {
        List<GenreDto> genres = genreService.findAll();

        if (genres.isEmpty()) {
            throw new NotFoundException();
        }
        return genres;
    }
}

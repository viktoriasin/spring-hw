package ru.otus.hw.rest.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.exceptions.NotFoundException;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorRestController {
    private final AuthorService authorService;

    @GetMapping("/api/v1/authors")
    public List<AuthorDto> getAuthors() {
        List<AuthorDto> authors = authorService.findAll();

        if (authors.isEmpty()) {
            throw new NotFoundException("Authors");
        }
        return authors;
    }
}

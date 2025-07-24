package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@RequiredArgsConstructor
@Component
public class GenreConverter {
    public String genreToString(GenreDto genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }

    public GenreDto genreToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}

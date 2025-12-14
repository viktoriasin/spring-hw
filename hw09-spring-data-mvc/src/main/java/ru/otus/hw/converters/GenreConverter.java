package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class GenreConverter {
    public GenreDto genreToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public static Set<Long> dtoGenresToGenresIds(List<GenreDto> genresDto) {
        return genresDto.stream()
            .map(GenreDto::getId)
            .collect(Collectors.toSet());
    }
}

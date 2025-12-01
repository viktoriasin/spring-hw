package ru.otus.hw.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Book;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private long id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;

    public static BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(), AuthorDto.toDto(book.getAuthor()), book.getGenres().stream().map(GenreDto::toDto).toList());
    }

    public static Book toModel(BookDto bookDto) {
        return new Book(bookDto.getId(), bookDto.getTitle(), AuthorDto.toModel(bookDto.getAuthor()), bookDto.getGenres().stream().map(GenreDto::toModel).toList());
    }
}

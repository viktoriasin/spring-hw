package ru.otus.hw.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.forms.BookForm;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.rest.exceptions.BookNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.rest.GlobalExceptionHandler.ERROR_STRING;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    @Autowired
    private MockMvc mvc;

    private final List<BookDto> books = List.of(
        new BookDto(1L, "Title1", new AuthorDto(1L, "Author"), List.of(new GenreDto(1L, "Genre1"))),
        new BookDto(2L, "Title2", new AuthorDto(2L, "Author2"), List.of(new GenreDto(2L, "Genre2")))
    );

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private AuthorService authorService;
    private final List<GenreDto> genres = List.of(
        books.get(0).getGenres().get(0),
        books.get(1).getGenres().get(0)
    );
    private BookDto bookDto = books.get(0);
    private BookForm bookForm = new BookForm(bookDto.getId(),
        bookDto.getTitle(),
        bookDto.getAuthor().getId(),
        bookDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()));
    private final List<AuthorDto> authors = List.of(
        books.get(0).getAuthor(),
        books.get(1).getAuthor()
    );
    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldReturnAllBooks() throws Exception {
        when(bookService.findAll()).thenReturn(books);

        String expectedResult = mapper.writeValueAsString(books);

        mvc.perform(get("/api/v1/books").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedResult));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        when(bookService.findById(1L)).thenReturn(bookDto);
        String expectedResult = mapper.writeValueAsString(bookDto);
        mvc.perform(get("/api/v1/books/1"))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedResult));
        ;
    }

    @Test
    void shouldUpdateBookById() throws Exception {
        when(bookService.findById(1L)).thenReturn(bookDto);
        when(bookService.update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(),
            ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(bookDto);
        String expectedResult = mapper.writeValueAsString(books.get(0));
        BookForm form = new BookForm(
            books.get(0).getId(),
            books.get(0).getTitle(),
            books.get(0).getAuthor().getId(),
            books.get(0).getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );
        String formJson = mapper.writeValueAsString(form);

        mvc.perform(put("/api/v1/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(formJson))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedResult));
    }

    @Test
    void shouldReturnErrorStringWhenBookNotFound() throws Exception {
        given(bookService.findById(1L)).willThrow(BookNotFoundException.class);
        mvc.perform(get("/api/v1/books/1"))
            .andExpect(status().isNotFound())
            .andExpect(content().string(ERROR_STRING));
    }

    @Test
    void shouldCallDeleteServiceMethodWhenDeletingBook() throws Exception {

        mvc.perform(delete("/api/v1/books/1"))
            .andExpect(status().isOk());
        verify(bookService, times(1)).deleteById(1L);
    }

    @Test
    void shouldCallCreateServiceMethodWhenDeletingBook() throws Exception {
        BookForm form = new BookForm(
            books.get(0).getId(),
            books.get(0).getTitle(),
            books.get(0).getAuthor().getId(),
            books.get(0).getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );
        String formJson = mapper.writeValueAsString(form);

        mvc.perform(post("/api/v1/books").contentType(APPLICATION_JSON)
                .content(formJson))
            .andExpect(status().isOk());
        verify(bookService, times(1)).insert(bookForm.getTitle(), bookForm.getAuthorId(), bookForm.getGenresId());
    }

    @Test
    void shouldNotCreateBookWithFieldErrors() throws Exception {
        BookForm form = new BookForm(
            books.get(0).getId(),
            "",
            books.get(0).getAuthor().getId(),
            books.get(0).getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );
        String formJson = mapper.writeValueAsString(form);

        mvc.perform(post("/api/v1/books").contentType(APPLICATION_JSON)
                .content(formJson))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldNotUpdateBookWithFieldErrors() throws Exception {
        BookForm form = new BookForm(
            books.get(0).getId(),
            "",
            books.get(0).getAuthor().getId(),
            books.get(0).getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );
        String formJson = mapper.writeValueAsString(form);

        mvc.perform(put("/api/v1/books/1").contentType(APPLICATION_JSON)
                .content(formJson))
            .andExpect(status().is4xxClientError());
    }
}
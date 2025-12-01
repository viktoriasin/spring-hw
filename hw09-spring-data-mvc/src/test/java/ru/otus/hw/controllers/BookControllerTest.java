package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.forms.BookForm;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private AuthorService authorService;

    private List<BookDto> books = List.of(
        new BookDto(1L, "Title1", new AuthorDto(1L, "Author"), List.of(new GenreDto(1L, "Genre1"))),
        new BookDto(2L, "Title2", new AuthorDto(2L, "Author2"), List.of(new GenreDto(2L, "Genre2")))
    );
    BookDto bookDto = books.get(0);
    BookForm bookForm = new BookForm(bookDto.getId(),
        bookDto.getTitle(),
        bookDto.getAuthor().getId(),
        bookDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()));
    private List<GenreDto> genres = List.of(
        books.get(0).getGenres().get(0),
        books.get(1).getGenres().get(0)
    );
    private List<AuthorDto> authors = List.of(
        books.get(0).getAuthor(),
        books.get(1).getAuthor()
    );

    @Test
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findAll()).thenReturn(books);

        mvc.perform(get("/"))
            .andExpect(view().name("bookList"))
            .andExpect(model().attribute("books", books));
    }

    @Test
    void shouldRenderEditPageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(books.get(0)));
        when(genreService.findAll()).thenReturn(genres);
        when(authorService.findAll()).thenReturn(authors);

        mvc.perform(get("/edit").param("id", "1"))
            .andExpect(view().name("bookEdit"))
            .andExpect(model().attribute("allGenres", genres))
            .andExpect(model().attribute("allAuthors", authors))
            .andExpect(model().attribute("bookForm", bookForm));
    }

    @Test
    void shouldRenderErrorPageWhenBookNotFound() throws Exception {
        when(bookService.findById(1L)).thenThrow(new NotFoundException());
        mvc.perform(get("/edit").param("id", "1"))
            .andExpect(view().name("customError"));
    }

    @Test
    void shouldUpdateBookAndRedirectToContextPath() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(books.get(0)));
        mvc.perform(post("/edit").param("id", "1")
                .param("title", "book1")
                .param("authorId", "1")
                .param("genresId", "1"))
            .andExpect(view().name("redirect:/"));

        verify(bookService, times(1)).update(1L, "book1", 1L, Set.of(1L));
    }

    @Test
    void shouldNotUpdateBookWithFieldErrorsAndRedirectToEditPage() throws Exception {
        mvc.perform(post("/edit")
                .param("id", "1")
                .param("title", "")
                .param("authorId", "1")
                .param("genresId", "1"))
            .andExpect(view().name("bookEdit"));

        verify(bookService, times(0)).update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(),
            ArgumentMatchers.anyLong(), ArgumentMatchers.any());
    }

    @Test
    void shouldRenderCreatePageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(books.get(0)));
        when(genreService.findAll()).thenReturn(genres);
        when(authorService.findAll()).thenReturn(authors);

        mvc.perform(get("/create"))
            .andExpect(view().name("bookCreate"))
            .andExpect(model().attribute("allGenres", genres))
            .andExpect(model().attribute("allAuthors", authors))
            .andExpect(model().attribute("bookForm", new BookForm()));
    }

    @Test
    void shouldSaveookAndRedirectToContextPath() throws Exception {
        mvc.perform(post("/create")
                .param("title", "book1")
                .param("authorId", "1")
                .param("genresId", "1"))
            .andExpect(view().name("redirect:/"));

        verify(bookService, times(1)).insert("book1", 1L, Set.of(1L));
    }

    @Test
    void shouldNotCreateBookWithFieldErrorsAndRedirectToEditPage() throws Exception {
        mvc.perform(post("/create")
                .param("title", "")
                .param("authorId", "1")
                .param("genresId", "1"))
            .andExpect(view().name("bookCreate"));

        verify(bookService, times(0)).insert(ArgumentMatchers.any(),
            ArgumentMatchers.anyLong(), ArgumentMatchers.any());
    }

    @Test
    void shouldDeleteBookById() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(bookDto));
        mvc.perform(post("/delete").param("id", "1"))
            .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).deleteById(bookDto.getId());
    }

    @Test
    void shouldRenderErrorPageWhenBookNotFoundWhenDelete() throws Exception {
        when(bookService.findById(1L)).thenThrow(new NotFoundException());
        mvc.perform(post("/delete").param("id", "1"))
            .andExpect(view().name("customError"));
    }
}
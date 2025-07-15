package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(jdbc.query("""
            select books.id as book_id
            , title
            , authors.id as author_id
            , full_name
            , genres.id as genre_id
            , name
            from books
            join authors on books.author_id = authors.id and books.id = :id
            join books_genres on books_genres.book_id = books.id
            join genres on genres.id = books_genres.genre_id
            """, Map.of("id", id), new JdbcBookRepository.BookResultSetExtractor()));
    }


    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books where id = :id", Map.of("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbc.query("select books.id as book_id, title, authors.id as author_id, full_name "
                + " from books join authors on books.author_id = authors.id",
            new JdbcBookRepository.BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("select book_id, genre_id from books_genres",
            new JdbcBookRepository.BookGenreRelationMapper());
    }


    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres, List<BookGenreRelation> relations) {
        Map<Long, Genre> genresMap = genres.stream()
            .collect(Collectors.toMap(Genre::getId, Function.identity()));
        Map<Long, Book> bookMap = booksWithoutGenres.stream()
            .collect(Collectors.toMap(Book::getId, Function.identity()));
        relations.forEach(bookGenreRelation -> {
            long bookId = bookGenreRelation.bookId;
            long genreId = bookGenreRelation.genreId;
            Book book = bookMap.get(bookId);
            if (book.getGenres() == null) {
                book.setGenres(new ArrayList<>());
            }
            book.getGenres().add(genresMap.get(genreId));
        });
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource paramsBook = new MapSqlParameterSource();
        paramsBook.addValue("title", book.getTitle());
        paramsBook.addValue("author_id", book.getAuthor().getId());

        jdbc.update("insert into books (title, author_id) values (:title, :author_id)", paramsBook, keyHolder);
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        MapSqlParameterSource paramsBook = new MapSqlParameterSource();
        paramsBook.addValue("id", book.getId());
        paramsBook.addValue("title", book.getTitle());
        paramsBook.addValue("author_id", book.getAuthor().getId());

        int recordsAffected = jdbc.update("update books set title = :title, " +
            "author_id = :author_id where id = :id", paramsBook);
        if (recordsAffected == 0) {
            throw new EntityNotFoundException("По ключу " + book.getId()
                + " в таблице books не нашлось ни одной записи.");
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        long bookId = book.getId();
        List<Genre> genreList = book.getGenres();
        SqlParameterSource[] array = genreList.stream()
            .map(genre -> new MapSqlParameterSource(Map.of("book_id", bookId, "genre_id", genre.getId())))
            .toArray(MapSqlParameterSource[]::new);
        jdbc.batchUpdate("insert into books_genres values (:book_id, :genre_id)", (SqlParameterSource[]) array);
    }


    private void removeGenresRelationsFor(Book book) {
        long bookId = book.getId();
        jdbc.update("delete from books_genres where book_id = :book_id", Map.of("book_id", bookId));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            String fullName = rs.getString("full_name");

            return new Book(bookId, title, new Author(authorId, fullName), null);
        }
    }

    private static class BookGenreRelationMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            long genreId = rs.getLong("genre_id");
            return new BookGenreRelation(bookId, genreId);
        }
    }

    // Использовать для findById
    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws DataAccessException, SQLException {
            List<Genre> genresList = new ArrayList<>();
            Book book = new Book();
            while (rs.next()) {
                if (book.getId() == 0) {
                    book.setId(rs.getLong("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(new Author(rs.getLong("author_id"), rs.getString("full_name")));
                }
                genresList.add(new Genre(rs.getLong("genre_id"), rs.getString("name")));
            }
            book.setGenres(genresList);
            return book.getId() > 0 ? book : null;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}

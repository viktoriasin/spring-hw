package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(long id);

    List<CommentDto> findByBookId(long id);

    CommentDto insert(String text, Book book);

    CommentDto update(long id, String text, Book book);

    void deleteById(long id);
}

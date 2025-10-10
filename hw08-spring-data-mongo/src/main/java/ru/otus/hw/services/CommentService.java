package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(String id);

    List<CommentDto> findByBookId(String id);

    CommentDto insert(String text, Book book);

    CommentDto update(String id, String text);

    void deleteById(String id);
}

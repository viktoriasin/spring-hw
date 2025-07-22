package ru.otus.hw.services;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(long id);

    Optional<Comment> findByBookId(long id);

    Comment insert(String text, Book book);

    Comment update(long id, String text, Book book);

    void deleteById(long id);
}

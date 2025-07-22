package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findByBookId(long id) {
        return commentRepository.findByBookId(id);
    }

    @Override
    public Comment insert(String text, Book book) {
        return commentRepository.save(new Comment(0, text, book));
    }

    @Override
    public Comment update(long id, String text, Book book) {
        return commentRepository.save(new Comment(id, text, book));
    }

    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}

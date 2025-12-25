package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.rest.dto.CommentDto;
import ru.otus.hw.rest.exceptions.BookNotFoundException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        return commentOptional.map(CommentDto::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findByBookId(long id) {
        List<Comment> comments = commentRepository.findByBookId(id);
        return comments.stream().map(CommentDto::toDto).toList();
    }

    @Override
    @Transactional
    public CommentDto insert(String text, Book book) {
        if (bookRepository.findById(book.getId()).isEmpty()) {
            throw new BookNotFoundException("Book with id %d for saving comment is not found!".formatted(book.getId()));
        }
        return CommentDto.toDto(commentRepository.save(new Comment(0, text, book)));
    }

    @Override
    @Transactional
    public CommentDto update(long id, String text) {
        Comment comment = commentRepository.findById(id).orElseThrow(
            () -> new BookNotFoundException("Comment with id %d is not found!".formatted(id)));
        comment.setText(text);
        return CommentDto.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}

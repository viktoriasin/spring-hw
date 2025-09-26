package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentConverter commentConverter;

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        return commentOptional.map(commentConverter::commentToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findByBookId(long id) {
        List<Comment> comments = commentRepository.findByBookId(id);
        return comments.stream().map(commentConverter::commentToDto).toList();
    }

    @Override
    @Transactional
    public CommentDto insert(String text, Book book) {
        if (bookRepository.findById(book.getId()).isEmpty()) {
            throw new EntityNotFoundException("Book with id %d for saving comment is not found!".formatted(book.getId()));
        }
        return commentConverter.commentToDto(commentRepository.save(new Comment(0, text, book)));
    }

    @Override
    @Transactional
    public CommentDto update(long id, String text) {
        Comment comment = commentRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Comment with id %d is not found!".formatted(id)));
        comment.setText(text);
        return commentConverter.commentToDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}

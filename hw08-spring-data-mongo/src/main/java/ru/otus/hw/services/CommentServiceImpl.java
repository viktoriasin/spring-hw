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
    public Optional<CommentDto> findById(String id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        return commentOptional.map(commentConverter::commentToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findByBookId(String id) {
        List<Comment> comments = commentRepository.findByBookId(id);
        return comments.stream().map(commentConverter::commentToDto).toList();
    }

    @Override
    public CommentDto insert(String text, Book book) {
        Optional<Book> bookOpt = bookRepository.findById(book.getId());
        if (bookOpt.isEmpty()) {
            throw new EntityNotFoundException("Book with id %d for saving comment is not found!".formatted(book.getId()));
        }
        Book book1 = bookOpt.get();
        Comment comment = new Comment();
        comment.setText(text);
        comment.setBook(new Book(book1.getId(), book1.getTitle(), book1.getAuthor(), book1.getGenres()));

        return commentConverter.commentToDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto update(String id, String text) {
        Comment comment = commentRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Comment with id %d is not found!".formatted(id)));
        comment.setText(text);
        return commentConverter.commentToDto(commentRepository.save(comment));
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteByBookId(String id) {
        commentRepository.deleteAllByBookId(id);
    }
}

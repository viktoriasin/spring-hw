package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    private final BookConverter bookConverter;

    public CommentDto commentToDto(Comment comment) {
        BookDto bookDto = bookConverter.bookToDto(comment.getBook());
        return new CommentDto(comment.getId(), comment.getText(), bookDto);
    }
}

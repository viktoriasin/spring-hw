package ru.otus.hw.converters;

import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

public class CommentConverter {
    public CommentDto commentDtoToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getBook());
    }
}

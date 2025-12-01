package ru.otus.hw.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Comment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long id;

    private String text;

    private BookDto book;

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), BookDto.toDto(comment.getBook()));
    }

    public static Comment toModel(CommentDto commentDto) {
        return new Comment(commentDto.getId(), commentDto.getText(), BookDto.toModel(commentDto.getBook()));
    }
}

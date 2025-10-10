package ru.otus.hw.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import ru.otus.hw.models.Book;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document("comments")
public class Comment {
    @Id
    private long id;

    @Field(name = "text")
    private String text;

    @Field(name = "book")
    private Book book;
}

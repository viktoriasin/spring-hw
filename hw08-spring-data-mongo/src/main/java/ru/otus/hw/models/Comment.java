package ru.otus.hw.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document("comments")
public class Comment {
    @Id
    private String id;

    @Field(name = "text")
    private String text;

    @DocumentReference(lazy = true)
    private Book book;
}

package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.*;
import ru.otus.hw.models.Book;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "BOOK_ID_FK"))
    private Book book;
}

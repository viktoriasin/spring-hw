package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
@NamedEntityGraph(name="book-entity-graph", attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genres")})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "AUTHOR_ID_FK"))
    private Author author;

//    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(targetEntity = Genre.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "books_genres", joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", title='" + title + '\'' +
            '}';
    }
}

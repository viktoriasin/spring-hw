package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "AUTHOR_ID_FK"))
    private Author author;

    private List<Genre> genres;

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", title='" + title + '\'' +
            '}';
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Book book)) {
            return false;
        }

        return id == book.id && title.equals(book.title);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(id);
        result = 31 * result + title.hashCode();
        return result;
    }
}

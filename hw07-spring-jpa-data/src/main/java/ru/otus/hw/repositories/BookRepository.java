package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(value = "book-entity-graph", type = EntityGraph.EntityGraphType.LOAD)
    List<Book> findAll();

    @EntityGraph(value = "book-entity-graph-author-with-genres", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Book> findById(Long id);
}

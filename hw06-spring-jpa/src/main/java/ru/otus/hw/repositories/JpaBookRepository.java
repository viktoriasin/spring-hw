package ru.otus.hw.repositories;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-entity-graph");
        TypedQuery<Book> query = em.createQuery("select bk from Book bk where bk.id = :id", Book.class);
        query.setParameter("id", id)
            .setHint("javax.persistence.fetchgraph", entityGraph);
        return Optional.ofNullable(query.getSingleResult());

    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-entity-graph");
        return em.createQuery("select bk from Book bk", Book.class)
            .setHint("javax.persistence.fetchgraph", entityGraph)
            .getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        em.remove(em.getReference(Book.class, id));
    }
}

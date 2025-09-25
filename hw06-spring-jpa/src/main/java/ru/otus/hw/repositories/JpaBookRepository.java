package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {


    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id)); // не делаем через граф, там как выгружаем одну книгу и тут не волнует проблемы n+1

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
        try {
            Book reference = em.getReference(Book.class, id);
            em.remove(reference);
        } catch (EntityNotFoundException e) {
            log.info("Can not delete book by id {}. Book does not exist.", id);
        }
    }
}

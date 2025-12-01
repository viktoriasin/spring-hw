package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String>, CommentCustomRepository {

    List<Comment> findByBookId(String id);

    // Либо метод deleteAllByBookId можно реализовать проще, через Spring Data, а не через CommentCustomRepository
//    void deleteAllByBookId(String bookId);

}

package ru.otus.hw.services;

import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Test {

    private final MongoTemplate mt;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final AuthorRepository authorRepository;

    public void test() {
        MongoCollection<Document> books = mt.getCollection("books");
        System.out.println(books.countDocuments());

        List<Document> documents = mt.findAll(Document.class, "books");
        for (Document doc : documents) {
            System.out.println(doc.toJson()); // Prints the BSON Document as a JSON string
        }

        List<Document> authors = mt.findAll(Document.class, "authors");
        for (Document doc : authors) {
            System.out.println(doc.toJson()); // Prints the BSON Document as a JSON string
        }

        List<Document> genres = mt.findAll(Document.class, "authors");
        for (Document doc : genres) {
            System.out.println(doc.toJson()); // Prints the BSON Document as a JSON string
        }

//        Book book = new Book();
//        book.setAuthor(new Author());
//        genreRepository.insert(new Genre(1L, "tag"));
//        List<Document> documents = mt.findAll(Document.class, "genres");
//        for (Document doc : documents) {
//            System.out.println(doc.toJson()); // Prints the BSON Document as a JSON string
//        }


    }
}

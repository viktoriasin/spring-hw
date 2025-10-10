package ru.otus.hw.services;

import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Book;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Test {

    @Autowired
    private MongoTemplate mt;

    public void test() {
        mt.createCollection("books");
        MongoCollection<Document> books = mt.getCollection("books");
        System.out.println(books.countDocuments());

        List<Document> documents = mt.findAll(Document.class, "books");
        for (Document doc : documents) {
            System.out.println(doc.toJson()); // Prints the BSON Document as a JSON string
        }
    }
}

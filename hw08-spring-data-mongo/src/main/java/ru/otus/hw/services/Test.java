package ru.otus.hw.services;

import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Test {

    @Autowired
    private MongoTemplate mt;

    public void test() {
        mt.createCollection("books");
        MongoCollection<Document> books = mt.getCollection("books");
        System.out.println(books);
    }
}

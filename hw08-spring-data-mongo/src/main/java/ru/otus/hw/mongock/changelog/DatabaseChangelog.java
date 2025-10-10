package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "sinvic", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "sinvic")
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("authors");
        var doc = new Document().append("name", "Author_1");
        var doc2 = new Document().append("name", "Author_2");
        var doc3 = new Document().append("name", "Author_3");
        myCollection.insertMany(List.of(doc, doc2, doc3));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "sinvic")
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("genres");
        List<Document> genres = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            genres.add(new Document().append("name", "Genre_" + i));
        }
        myCollection.insertMany(genres);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "sinvic")
    public void insertBooks(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("books");

        myCollection.insertOne(new Document().append("title", "BookTitle_1"));
    }
}

//
//insert into books(title, author_id)
//values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);
//
//insert into books_genres(book_id, genre_id)
//values (1, 1),   (1, 2),
//    (2, 3),   (2, 4),
//    (3, 5),   (3, 6);
//
//insert into comments(text, book_id)
//values ('Very good book', 1), ('Very bad book', 1), ('Wonderful book!', 3);
//

package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "002")
public class BookChangelog {

    @ChangeSet(order = "001", id = "insertBooks", author = "sinvic")
    public void insertBooks(MongockTemplate mongockTemplate) {
        for (long i = 1; i < 3; i++) {
            Author author = mongockTemplate.findOne(new Query().limit(1), Author.class, "authors");
            Genre genre = mongockTemplate.findOne(new Query().limit(1), Genre.class, "genres");

            Book book = new Book();
            book.setTitle("BookTitle_" + i);
            book.setAuthor(author);
            List<Genre> genres = new ArrayList<>();
            genres.add(genre);
            book.setGenres(genres);

            mongockTemplate.save(book, "books");
        }
    }

    @ChangeSet(order = "002", id = "insertComments", author = "sinvic")
    public void insertComments(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("comments");
        var doc = new Document().append("text", "Very good book").append("book_id", 1);
        var doc2 = new Document().append("text", "Very bad book").append("book_id", 1);
        var doc3 = new Document().append("text", "Wonderful book!").append("book_id", 3);
        myCollection.insertMany(List.of(doc, doc2, doc3));
    }
}

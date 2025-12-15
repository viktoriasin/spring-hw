package ru.otus.hw.controllers;

public class BookNotFoundException extends RuntimeException{

    public BookNotFoundException() {
        super("Book not found");
    }
}

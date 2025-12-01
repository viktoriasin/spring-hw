package ru.otus.hw.rest.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException() {
        super("Book not found");
    }
}

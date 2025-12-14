package ru.otus.hw.controllers;

public class NotFoundException extends RuntimeException{

    public NotFoundException() {
        super("Book not found");
    }
}

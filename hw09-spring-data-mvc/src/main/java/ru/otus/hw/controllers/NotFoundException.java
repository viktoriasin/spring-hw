package ru.otus.hw.controllers;

public class NotFoundException extends RuntimeException{

    NotFoundException() {
        super("Book not found");
    }
}

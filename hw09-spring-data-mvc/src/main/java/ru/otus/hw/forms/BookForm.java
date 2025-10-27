package ru.otus.hw.forms;

import lombok.Data;

import java.util.List;

@Data
public class BookForm {
    private String title;
    private String authorId;
    private List<String> genresId;
}

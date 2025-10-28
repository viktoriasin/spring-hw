package ru.otus.hw.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookForm {

    @NotBlank(message = "{title-field-should-not-be-blank}")
    @Size(min = 2, max = 10, message = "{title-field-should-has-expected-size}")
    private String title;

    private String authorId;

    @NotEmpty(message = "{genres-field-should-not-be-empty}")
    private List<String> genresId;
}

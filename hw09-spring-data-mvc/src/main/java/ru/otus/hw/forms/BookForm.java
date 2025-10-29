package ru.otus.hw.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BookForm {

    private String id;

    @NotBlank(message = "{title-field-should-not-be-blank}")
    @Size(min = 2, max = 50, message = "{title-field-should-has-expected-size}")
    private String title;

    private String authorId;

    @NotEmpty(message = "{genres-field-should-not-be-empty}")
    private List<String> genresId;
}

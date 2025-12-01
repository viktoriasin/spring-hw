package ru.otus.hw.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BookForm {

    private Long id;

    @NotBlank(message = "{title-field-should-not-be-blank}")
    @Size(min = 2, max = 50, message = "{title-field-should-has-expected-size}")
    private String title;

    @NonNull
    private Long authorId;

    @NotEmpty(message = "{genres-field-should-not-be-empty}")
    private Set<@NonNull Long> genresId;
}

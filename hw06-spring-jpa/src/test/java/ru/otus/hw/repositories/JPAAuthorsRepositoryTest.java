package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами ")
@DataJpaTest
@Import(JPAAuthorRepository.class)
class JPAAuthorsRepositoryTest {

    private static final int EXPECTED_NUMBER_OF_AUTHORS = 3;

    @Autowired
    private JPAAuthorRepository repositoryJPA;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загрузить автора по заданному id")
    @Test
    void shouldReturnCorrectAuthorsListById() {
        val optionalActualAuthor = repositoryJPA.findById(1L);
        val expectedAuthor= em.find(Author.class, 1L);

        assertThat(optionalActualAuthor).isPresent().get()
            .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = repositoryJPA.findAll();
        assertThat(actualAuthors).isNotNull().hasSize(EXPECTED_NUMBER_OF_AUTHORS)
            .allMatch(s -> !s.getFullName().isEmpty());
    }
}

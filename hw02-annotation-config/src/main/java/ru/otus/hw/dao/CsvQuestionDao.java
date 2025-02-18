package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        return getQuestionDtoFromCsvFile(fileNameProvider.getTestFileName())
                .stream()
                .map(QuestionDto::toDomainObject)
                .collect(Collectors.toList());
    }

    private List<QuestionDto> getQuestionDtoFromCsvFile(String testFileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        try (var in = new InputStreamReader(Objects.requireNonNull(classLoader.getResourceAsStream(testFileName)))) {
            return new CsvToBeanBuilder<QuestionDto>(in)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException nullPointerException) {
            throw new QuestionReadException("File " + testFileName + " could not be found.");
        } catch (Exception e) {
            throw new QuestionReadException(e.getMessage());
        }
    }
}

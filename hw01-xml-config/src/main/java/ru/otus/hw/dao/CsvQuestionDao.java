package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        return getQuestionDtoFromCsvFile(getCsvFile())
                .stream()
                .map(QuestionDto::toDomainObject)
                .collect(Collectors.toList());
    }

    private List<QuestionDto> getQuestionDtoFromCsvFile(File csvFile) {
        try {
            return new CsvToBeanBuilder<QuestionDto>(new FileReader(csvFile))
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new QuestionReadException(e.getMessage());
        }
    }

    private File getCsvFile() {
        String testFileName = fileNameProvider.getTestFileName();
        URL resource = getClass().getClassLoader().getResource(testFileName);
        if (resource == null) {
            throw new QuestionReadException("File " + testFileName + " not found!");
        } else {
            try {
                return new File(resource.toURI());
            } catch (URISyntaxException e) {
                throw new QuestionReadException(e.getMessage());
            }
        }
    }
}

package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    private CsvQuestionDao questionDao;

    @Test
    public void testGetInputStreamFromCorrectFile() {
        when(fileNameProvider.getTestFileName()).thenReturn("questions.csv");
        questionDao = new CsvQuestionDao(fileNameProvider);
        List<Question> all = questionDao.findAll();
        assertEquals(all.size(), 2);
        assertFalse(all.get(0).text().isEmpty());
        assertFalse(all.get(0).answers().isEmpty());
    }

    @Test
    public void testGetInputStreamFromIncorrectFile() {
        when(fileNameProvider.getTestFileName()).thenReturn("questions1.csv");
        questionDao = new CsvQuestionDao(fileNameProvider);
        QuestionReadException questionReadException = assertThrowsExactly(QuestionReadException.class, () -> questionDao.findAll());
        assertEquals("File questions1.csv could not be found.", questionReadException.getMessage());
    }
}
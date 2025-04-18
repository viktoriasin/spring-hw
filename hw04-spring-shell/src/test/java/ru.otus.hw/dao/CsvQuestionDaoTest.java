package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CsvQuestionDao.class)
class CsvQuestionDaoTest {

    @MockitoBean
    private TestFileNameProvider fileNameProvider;

    @Autowired
    private QuestionDao questionDao;

    @Test
    public void testGetInputStreamFromCorrectFile() {
        when(fileNameProvider.getTestFileName()).thenReturn("questions.csv");
        List<Question> all = questionDao.findAll();
        assertEquals(all.size(), 2);
        assertFalse(all.get(0).text().isEmpty());
        assertFalse(all.get(0).answers().isEmpty());
    }

    @Test
    public void testGetInputStreamFromIncorrectFile() {
        when(fileNameProvider.getTestFileName()).thenReturn("questions1.csv");
        QuestionReadException questionReadException = assertThrowsExactly(QuestionReadException.class, () -> questionDao.findAll());
        assertEquals("File questions1.csv could not be found.", questionReadException.getMessage());
    }
}
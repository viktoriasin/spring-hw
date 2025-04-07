package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private LocalizedIOService ioService;
    @Mock
    private QuestionDao questionDao;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("Ivan", "Ivanov");
        Answer answer1 = new Answer("Science doesn't know this yet", true);
        Answer answer2 = new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false);
        Answer answer3 = new Answer("Absolutely not", false);
        Question question = new Question("Is there life on Mars?", List.of(answer1, answer2, answer3));

        when(questionDao.findAll()).thenReturn(List.of(question));
    }

    @Test
    void testCorrectStudentAnswer() {
        when(ioService.readIntForRangeWithPromptLocalized(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),  ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString()))
                .thenReturn(1);

        TestServiceImpl testService = new TestServiceImpl(ioService, questionDao);
        TestResult testResult = testService.executeTestFor(student);
        assertEquals(testResult.getRightAnswersCount(), 1);
        assertEquals(testResult.getAnsweredQuestions().size(), 1);
    }

    @Test
    void testIncorrectStudentAnswer() {
        when(ioService.readIntForRangeWithPromptLocalized(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),  ArgumentMatchers.anyString(),
            ArgumentMatchers.anyString()))
                .thenReturn(3);

        TestServiceImpl testService = new TestServiceImpl(ioService, questionDao);
        TestResult testResult = testService.executeTestFor(student);
        assertEquals(testResult.getRightAnswersCount(), 0);
        assertEquals(testResult.getAnsweredQuestions().size(), 1);
    }
}
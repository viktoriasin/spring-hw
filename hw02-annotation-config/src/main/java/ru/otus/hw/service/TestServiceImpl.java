package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        int questionCount = 0;
        for (var question : questions) {
            ioService.printFormattedLine("Question №%d: %s", ++questionCount, question.text());
            if (question.answers() != null && !question.answers().isEmpty()) {
                int validAnswerNumber = printAnswersAndDetectRightAnswerNumber(question.answers());
                testResult.applyAnswer(question, validAnswerNumber == getStudentAnswer(question.answers().size()));
            }
            ioService.printLine("");
        }
        return testResult;
    }

    private int printAnswersAndDetectRightAnswerNumber(List<Answer> answers) {
        ioService.printFormattedLine("Possible answers:");
        int answerCount = 0;
        int validAnswerNumber = -1;
        for (Answer answer : answers) {
            ioService.printFormattedLine("%d) %s", ++answerCount, answer.text());
            if (answer.isCorrect()) {
                validAnswerNumber = answerCount;
            }
        }
        return validAnswerNumber;
    }

    private int getStudentAnswer(int answerCount) {
        String errorMessage = "You must enter the number of one of the answer options";
        return ioService.readIntForRangeWithPrompt(1, answerCount, "Enter your question: ", errorMessage);
    }
}

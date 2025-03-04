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
            List<Answer> answers = question.answers();
            if (answers != null && !answers.isEmpty()) {
                printAnswers(answers);
                int studentAnswerNumber = getStudentAnswerNumber(answers.size());
                testResult.applyAnswer(question, answers.get(studentAnswerNumber - 1).isCorrect());
            }
            ioService.printLine("");
        }
        return testResult;
    }

    private void printAnswers(List<Answer> answers) {
        ioService.printFormattedLine("Possible answers:");
        int answerCount = 0;
        for (Answer answer : answers) {
            ioService.printFormattedLine("%d) %s", ++answerCount, answer.text());
        }
    }

    private int getStudentAnswerNumber(int answerCount) {
        String errorMessage = "You must enter the number of one of the answer options";
        return ioService.readIntForRangeWithPrompt(1, answerCount, "Enter your answer: ", errorMessage);
    }
}

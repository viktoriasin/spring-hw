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

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        int questionCount = 0;
        for (var question : questions) {
            ioService.printLine(question.text());
            ioService.printFormattedLineLocalized("TestService.question", ++questionCount, question.text());
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
        ioService.printLineLocalized("TestService.possible.answers");
        int answerCount = 0;
        for (Answer answer : answers) {
            ioService.printFormattedLineLocalized("TestService.possible.answers.text", ++answerCount, answer.text());
        }
    }

    private int getStudentAnswerNumber(int answerCount) {
        return ioService.readIntForRangeWithPromptLocalized(1, answerCount,
            "TestService.possible.answers.enter", "TestService.possible.error.message");
    }

}

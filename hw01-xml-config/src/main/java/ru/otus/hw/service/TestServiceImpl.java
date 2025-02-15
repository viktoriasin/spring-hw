package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        int questionCount = 0;
        for (Question question : questionDao.findAll()) {
            ioService.printFormattedLine("Question №%d: %s", ++questionCount, question.text());
            if (question.answers() != null && !question.answers().isEmpty()) {
                ioService.printFormattedLine("Possible answers:");
                int answerCount = 0;
                for (Answer answer : question.answers()) {
                    ioService.printFormattedLine("%d) %s", ++answerCount, answer.text());
                }
            }
            ioService.printLine("");
        }
    }
}

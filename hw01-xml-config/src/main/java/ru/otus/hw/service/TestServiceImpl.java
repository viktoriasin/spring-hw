package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов
        List<Question> questionList = questionDao.findAll();
        int questionCount = 0;
        for (Question question : questionList) {
            ioService.printFormattedLine("Question №%d: %s", ++questionCount, question.text());
            if (question.answers() != null) {
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

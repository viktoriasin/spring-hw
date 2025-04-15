package ru.otus.hw.domain;

import ru.otus.hw.domain.Answer;

import java.util.List;

public record Question(String text, List<Answer> answers) {
}

package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.shell.ShellInputReader;
import ru.otus.hw.shell.ShellOutputWriter;

@RequiredArgsConstructor
public class ConsoleIOService implements IOService {
    private static final int MAX_ATTEMPTS = 10;

    private final ShellInputReader shellInputReader;

    private final ShellOutputWriter shellOutputWriter;

    @Override
    public void printLine(String s) {
        shellOutputWriter.printLine(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        shellOutputWriter.printFormattedLine(s, args);
    }

    @Override
    public String readString() {
        return shellInputReader.readString();
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        return shellInputReader.readStringWithPrompt(prompt);
    }

    @Override
    public int readIntForRange(int min, int max, String errorMessage) {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            try {
                var stringValue = readString();
                int intValue = Integer.parseInt(stringValue);
                if (intValue < min || intValue > max) {
                    throw new IllegalArgumentException();
                }
                return intValue;
            } catch (IllegalArgumentException e) {
                printLine(errorMessage);
            }
        }
        throw new IllegalArgumentException("Error during reading int value");
    }

    @Override
    public int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
        printLine(prompt);
        return readIntForRange(min, max, errorMessage);
    }
}

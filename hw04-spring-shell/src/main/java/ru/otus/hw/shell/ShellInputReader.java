package ru.otus.hw.shell;

import org.jline.reader.LineReader;


public class ShellInputReader {

    private final LineReader lineReader;

    public ShellInputReader(LineReader lineReader) {

        this.lineReader = lineReader;
    }

    public String readString() {
        return lineReader.readLine();
    }

    public String readStringWithPrompt(String prompt) {
        return lineReader.readLine(prompt + ": ");
    }

    int readIntForRange(int min, int max, String errorMessage) {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            try {
                var stringValue = readString();
                int intValue = Integer.parseInt(stringValue);
                if (intValue < min || intValue > max) {
                    throw new IllegalArgumentException();
                }
                return intValue;
            } catch (IllegalArgumentException e) {
                (errorMessage);
            }
        }
        throw new IllegalArgumentException("Error during reading int value");
    }

    int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage);

    public String prompt(String prompt) {
        return prompt(prompt, null);
    }


    public String prompt(String prompt, String defaultValue) {
        String answer = "";
        answer = lineReader.readLine(prompt + ": ");
        if (answer.isEmpty()) {
            return defaultValue;
        }
        return answer;
    }
}
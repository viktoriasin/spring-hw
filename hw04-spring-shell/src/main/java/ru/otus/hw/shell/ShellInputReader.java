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
}
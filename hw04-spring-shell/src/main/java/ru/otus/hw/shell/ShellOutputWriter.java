package ru.otus.hw.shell;

import org.jline.terminal.Terminal;

public class ShellOutputWriter {

    private final Terminal terminal;

    public ShellOutputWriter(Terminal terminal) {
        this.terminal = terminal;
    }

    public void printLine(String s) {
        terminal.writer().println(s);
        terminal.flush();
    }

    public void printFormattedLine(String s, Object... args) {
        terminal.writer().println(String.format(s + "%n", args));
        terminal.flush();
    }
}

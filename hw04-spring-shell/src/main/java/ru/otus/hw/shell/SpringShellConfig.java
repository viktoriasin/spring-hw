package ru.otus.hw.shell;

import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class SpringShellConfig {

    @Bean
    public ShellInputReader inputReader(@Lazy LineReader lineReader) {
        return new ShellInputReader(lineReader);
    }

    @Bean
    public ShellOutputWriter outputWriter(@Lazy Terminal terminal) {
        return new ShellOutputWriter(terminal);
    }

}

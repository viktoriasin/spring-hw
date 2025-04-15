package hw.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public interface TestRunnerService extends ApplicationRunner {
    void run(ApplicationArguments applicationArguments);
}

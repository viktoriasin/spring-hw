package ru.otus.hw.shell;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.TestService;

@ShellComponent
@RequiredArgsConstructor
public class StudentTestingCommands {

    private final ShellInputReader shellInputReader;

    private final LoginContext loginContext;

    private final TestService testService;

    private final ResultService resultService;


    @ShellMethod(value = "Login a student for testing", key = {"login", "l"})
    public String registerStudent() {
        String firstName = shellInputReader.readStringWithPrompt("Enter your name");
        String lastNameName = shellInputReader.readStringWithPrompt("Enter your last name");
        loginContext.login(new Student(firstName, lastNameName));
        return "Successfully login student " + lastNameName + " " + firstName + " for testing!";
    }

    @ShellMethod(value = "Run test questions for given student", key = {"execute-test", "e"})
    @ShellMethodAvailability(value = "isStudentLoggedIn")
    public String executeTestFor() {
        Student student = loginContext.getCurrentLoggedInStudent();
        TestResult testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
        return "Testing is completed!";
    }

    private Availability isStudentLoggedIn() {
        return loginContext.isStudentLoggedIn()
            ? Availability.available()
            : Availability.unavailable("Login student first");
    }
}

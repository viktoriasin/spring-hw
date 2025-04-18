package ru.otus.hw.shell;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.domain.Student;
import ru.otus.hw.security.LoginContext;

@ShellComponent
@RequiredArgsConstructor
public class StudentTestingCommands {

    private final ShellInputReader shellInputReader;

    private final LoginContext loginContext;


    @ShellMethod(value = "Login a student for testing", key = {"login", "l"})
    public String registerStudent() {
        String firstName = shellInputReader.prompt("Enter your name");
        String lastNameName = shellInputReader.prompt("Enter your last name");
        loginContext.login(new Student(firstName, lastNameName));
        return "Successfully login student " + lastNameName + " " + firstName + " for testing!";
    }

    @ShellMethod(value = "Run test questions for given student", key = {"execute-test", "e"})
    @ShellMethodAvailability(value = "isStudentLoggedIn")
    public String executeTestFor() {

        return "Successfully register student " + student.getFullName() + " for testing!";
    }

    private Availability isStudentLoggedIn() {
        return loginContext.isStudentLoggedIn()
            ? Availability.available()
            : Availability.unavailable("Login student first");
    }

}

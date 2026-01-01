package ru.otus.hw.page;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookPageController {

    @GetMapping("/")
    public String listAllBookPage() {
        return "bookList";
    }

    @GetMapping("/edit")
    public String editBookPage(@RequestParam("id") long id) {
        return "bookEdit";
    }

    @GetMapping("/create")
    public String createBook() {
        return "bookCreate";
    }
}

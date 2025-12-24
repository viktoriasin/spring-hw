package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BookService bookService;

    @GetMapping("/comments")
    public String getCommentsPage(@RequestParam("bookId") long id, Model model) {
        List<CommentDto> comments = commentService.findByBookId(id);
        BookDto bookDto = bookService.findById(id);

        model.addAttribute("book", bookDto);
        model.addAttribute("comments", comments);

        return "bookWithComments";
    }
}

package cdss.assignment2.backend.controller;

import cdss.assignment2.backend.dto.BookCreationRequest;
import cdss.assignment2.backend.dto.TextCreationRequest;
import cdss.assignment2.backend.model.Book;
import cdss.assignment2.backend.model.Text;
import cdss.assignment2.backend.services.BookService;
import cdss.assignment2.backend.services.TextService;
import cdss.assignment2.backend.utils.XssUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("book")
@RestController
public class BookController {

    public BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Book createText(@RequestBody BookCreationRequest bookCreationRequest) {
        return this.bookService.createBook(bookCreationRequest);
    }


    @GetMapping
    public List<Book> getAll() {
        return bookService.getAll();
    }
}

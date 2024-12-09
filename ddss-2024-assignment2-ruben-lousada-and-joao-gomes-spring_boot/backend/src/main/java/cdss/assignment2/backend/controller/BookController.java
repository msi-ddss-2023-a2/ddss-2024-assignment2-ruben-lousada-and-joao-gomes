package cdss.assignment2.backend.controller;

import cdss.assignment2.backend.dto.BookCreationRequest;
import cdss.assignment2.backend.dto.TextCreationRequest;
import cdss.assignment2.backend.model.Book;
import cdss.assignment2.backend.model.Text;
import cdss.assignment2.backend.services.BookService;
import cdss.assignment2.backend.services.TextService;
import cdss.assignment2.backend.utils.XssUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RequestMapping("book")
@RestController
public class BookController {

    public final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Book createBook(@RequestBody BookCreationRequest bookCreationRequest) {
        return this.bookService.createBook(bookCreationRequest);
    }


    @GetMapping
    public List<Book> getAll() {
        return bookService.getAll();
    }

    @GetMapping(path = "secure")
    public List<Book> getBooksSecure(@RequestParam(required = false) String title,
                               @RequestParam(required = false) String author,
                               @RequestParam(required = false) String category,
                               @RequestParam(required = false) Double priceMore,
                               @RequestParam(required = false) Double priceLess,
                               @RequestParam(required = false) String text,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date until,
                               @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        return this.bookService.getFilterSecure(title, author,category, priceMore, priceLess, text, from, until, limit);
    }

    @GetMapping(path = "insecure")
    public List<Book> getBooksInsecure(@RequestParam(required = false) String title,
                                     @RequestParam(required = false) String author,
                                     @RequestParam(required = false) String category,
                                     @RequestParam(required = false) Double priceMore,
                                     @RequestParam(required = false) Double priceLess,
                                     @RequestParam(required = false) String text,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date until,
                                     @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        return this.bookService.getFilterInsecure(title, author,category, priceMore, priceLess, text, from, until, limit);
    }
}

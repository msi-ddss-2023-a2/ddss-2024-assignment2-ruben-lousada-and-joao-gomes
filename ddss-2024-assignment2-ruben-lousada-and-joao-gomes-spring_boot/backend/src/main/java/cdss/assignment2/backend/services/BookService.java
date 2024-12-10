package cdss.assignment2.backend.services;

import cdss.assignment2.backend.dto.BookCreationRequest;
import cdss.assignment2.backend.dto.TextCreationRequest;
import cdss.assignment2.backend.model.Book;
import cdss.assignment2.backend.model.Text;
import cdss.assignment2.backend.repository.BookRepository;
import cdss.assignment2.backend.repository.BookRepositoryCustomImpl;
import cdss.assignment2.backend.repository.TextRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BookService {

    private final BookRepositoryCustomImpl bookRepositoryCustom;
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository, BookRepositoryCustomImpl bookRepositoryCustom) {
        this.bookRepository = bookRepository;
        this.bookRepositoryCustom = bookRepositoryCustom;
    }

    public Book createBook(BookCreationRequest bookCreationRequest) {
        Book book = new Book();
        book.setTitle(bookCreationRequest.getTitle());
        book.setAuthor(bookCreationRequest.getAuthor());
        book.setPrice(bookCreationRequest.getPrice());
        book.setDate(bookCreationRequest.getDate());
        book.setCategory(bookCreationRequest.getCategory());
        book.setText(bookCreationRequest.getText());
        return bookRepository.save(book);
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public List<Book> getFilterSecure(String title,
                                      String author,
                                      String category,
                                      Double priceMore,
                                      Double priceLess,
                                      String text,
                                      Date from,
                                      Date until,
                                      Integer limit
                                      ) {
        System.out.println(limit);
        Pageable pageable = PageRequest.of(0, limit > 100 ? 100 : limit);
        Page<Book> booksPage = bookRepository.findBooksSecure(title, author, category, priceMore, priceLess, from, until, pageable);
        return booksPage.getContent();

        // return this.bookRepository.findBooksSecure(
        //         title,
        //         author,
        //         category,
        //         priceMore,
        //         priceLess,
        //         //text
        //         from,
        //         until
        //         //limit
        // );
    }

    public List<Book> getFilterInsecure(String title,
                                       String author,
                                       String category,
                                       Double priceMore,
                                       Double priceLess,
                                       String text,
                                       Date from,
                                       Date until,
                                       Integer limit) {

        // System.out.println(title);
        // System.out.println(author);
        // System.out.println(category);
        // System.out.println(priceMore);
        // System.out.println(priceLess);
        // System.out.println(text);
        // System.out.println(from);
        // System.out.println(until);

        return this.bookRepository.findBooksInsecure(
                title,
                author,
                category,
                priceMore,
                priceLess,
                text,
                from,
                until,
                limit
        );
    }
}

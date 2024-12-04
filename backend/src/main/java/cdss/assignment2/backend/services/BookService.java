package cdss.assignment2.backend.services;

import cdss.assignment2.backend.dto.BookCreationRequest;
import cdss.assignment2.backend.dto.TextCreationRequest;
import cdss.assignment2.backend.model.Book;
import cdss.assignment2.backend.model.Text;
import cdss.assignment2.backend.repository.BookRepository;
import cdss.assignment2.backend.repository.TextRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(BookCreationRequest bookCreationRequest) {
        Book book = new Book();
        book.setTitle(bookCreationRequest.getTitle());
        book.setAuthor(bookCreationRequest.getAuthor());
        book.setPrice(bookCreationRequest.getPrice());
        book.setDate(bookCreationRequest.getDate());
        book.setCategory(bookCreationRequest.getCategory());
        return bookRepository.save(book);
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }
}

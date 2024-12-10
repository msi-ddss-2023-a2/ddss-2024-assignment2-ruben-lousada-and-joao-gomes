package cdss.assignment2.backend.repository;

import cdss.assignment2.backend.model.Book;
import cdss.assignment2.backend.model.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByCategory(String category);

    List<Book> findByPriceBetween(Double minPrice, Double maxPrice);

    // Combine multiple conditions
    List<Book> findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(String title, String author);
}
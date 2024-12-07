package cdss.assignment2.backend.repository;

import cdss.assignment2.backend.model.Book;
import cdss.assignment2.backend.model.Text;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT b FROM Book b WHERE (:title is NULL OR b.title LIKE :title)" +
            " AND (:author is NULL OR b.author LIKE :author)" +
            " AND (:category is NULL OR b.category LIKE :category)" +
            " AND (:priceMore is NULL OR b.price >= :priceMore)" +
            " AND (:priceLess is NULL OR b.price <= :priceLess)" +
            " AND (coalesce(:from, null) is NULL OR b.date >= :from) " +
            " AND (coalesce(:until, null) is NULL OR b.date <= :until) "
            )
    Page<Book> findBooksSecure(@Param("title") String title,
                               @Param("author") String author,
                               @Param("category") String category,
                               @Param("priceMore") Double priceMore,
                               @Param("priceLess") Double priceLess,
                               // @Param("text") String text
                               @Param("from") Date dateFrom,
                               @Param("until") Date dateUntil,
                               Pageable pageable
    );

    @Query(value = "SELECT * FROM Book b " +
            " WHERE (:title IS NULL OR b.title LIKE CONCAT(:title)) " +
            " AND (:author IS NULL OR b.author LIKE CONCAT(:author)) " +
            " AND (:category IS NULL OR b.category LIKE CONCAT(:category)) " +
            " AND (:priceMore IS NULL OR b.price >= :priceMore) " +
            " AND (:priceLess IS NULL OR b.price <= :priceLess) " +
            " AND (:text IS NULL OR b.text LIKE CONCAT(:text)) " +
            " AND (coalesce(:from, null) IS NULL OR b.date >= :from) " +
            " AND (coalesce(:until, null) IS NULL OR b.date >= :until) LIMIT :numberToShow", nativeQuery = true)
    List<Book> findBooksInsecure(@Param("title") String title,
                                 @Param("author") String author,
                                 @Param("category") String category,
                                 @Param("priceMore") Double priceMore,
                                 @Param("priceLess") Double priceLess,
                                 @Param("text") String text,
                                 @Param("from") Date dateFrom,
                                 @Param("until") Date until,
                                 @Param("numberToShow") Integer numberToShow
    );
}
package cdss.assignment2.backend.repository;

import cdss.assignment2.backend.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class BookRepositoryCustomImpl {


    @PersistenceContext
    private EntityManager entityManager;


    public List<Book> findBooksByDSecure(String title, String author, String category,
                                         Double priceMore, Double priceLess,
                                         String text, Date dateFrom, Date dateUntil) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();
        if (title != null) predicates.add(cb.like(root.get("title"), "%" + title + "%"));
        if (author != null) predicates.add(cb.like(root.get("author"), "%" + author + "%"));
        if (category != null) predicates.add(cb.like(root.get("category"), "%" + category + "%"));
        if (priceMore != null) predicates.add(cb.greaterThanOrEqualTo(root.get("price"), priceMore));
        if (priceLess != null) predicates.add(cb.lessThanOrEqualTo(root.get("price"), priceLess));
        if (text != null) predicates.add(cb.like(root.get("text"), "%" + text + "%"));
        if (dateFrom != null) predicates.add(cb.greaterThanOrEqualTo(root.get("date"), dateFrom));
        if (dateUntil != null) predicates.add(cb.lessThanOrEqualTo(root.get("date"), dateUntil));

        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }
}

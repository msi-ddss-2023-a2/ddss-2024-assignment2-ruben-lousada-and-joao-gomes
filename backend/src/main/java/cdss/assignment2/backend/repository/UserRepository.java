package cdss.assignment2.backend.repository;

import cdss.assignment2.backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Account, Integer> {

    @Query("SELECT u FROM Account u WHERE u.email LIKE :username")
    Optional<Account> findByUsername(String username);
}

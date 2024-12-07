package cdss.assignment2.backend.repository;

import cdss.assignment2.backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Account, Integer> {

    @Query("SELECT u FROM Account u WHERE u.email LIKE :username")
    Optional<Account> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE Account u SET u.failedLoginAttempts = u.failedLoginAttempts + 1 WHERE u.email = :username")
    void incrementFailedLoginAttempts(String username);

    @Transactional
    @Modifying
    @Query("UPDATE Account u SET u.failedLoginAttempts = 0 WHERE u.email = :username")
    void resetFailedLoginAttempts(String username);
}

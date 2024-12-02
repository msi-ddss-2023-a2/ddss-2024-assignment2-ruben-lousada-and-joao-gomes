package cdss.assignment2.backend.repository;

import cdss.assignment2.backend.model.Account;
import cdss.assignment2.backend.model.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TextRepository extends JpaRepository<Text, Integer> {

}

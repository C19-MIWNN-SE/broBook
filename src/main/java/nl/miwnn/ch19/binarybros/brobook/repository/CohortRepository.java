package nl.miwnn.ch19.binarybros.brobook.repository;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Paul Rademaker
 */

public interface CohortRepository extends JpaRepository<Cohort, Long> {
    Optional<Cohort> findAllByUsername(String username);

}

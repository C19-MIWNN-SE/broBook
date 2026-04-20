package nl.miwnn.ch19.binarybros.brobook.repository;

import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Paul Rademaker
 */

public interface  CohortRepository extends JpaRepository<Cohort, Long> {
//    List<Cohort> findAllByUsername(String username);

}

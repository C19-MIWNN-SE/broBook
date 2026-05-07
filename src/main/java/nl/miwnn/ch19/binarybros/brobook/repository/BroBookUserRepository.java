package nl.miwnn.ch19.binarybros.brobook.repository;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Paul Rademaker
 */

public interface BroBookUserRepository extends JpaRepository<BroBookUser, Long> {
    @Query("SELECT DISTINCT u FROM BroBookUser u JOIN u.cohorts c " +
            "WHERE c.id = :cohortId " +
            "AND (u.firstName LIKE %:search% OR u.lastName LIKE %:search%)")
    Page<BroBookUser> findByCohortAndSearch(@Param("cohortId") Long cohortId,
                                            @Param("search") String search,
                                            Pageable pageable);


    Optional<BroBookUser> findByUsername(String username);
    List<BroBookUser> findByRole(String role);
    Page<BroBookUser> findByFirstNameContainingOrLastNameContainingAllIgnoreCase(
            String firstName, String lastName, Pageable pageable);
    List<BroBookUser> findByRole(Role role);

    boolean existsByUsername(String username);
}

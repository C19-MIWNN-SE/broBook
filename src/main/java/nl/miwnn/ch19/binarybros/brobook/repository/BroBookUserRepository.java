package nl.miwnn.ch19.binarybros.brobook.repository;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Paul Rademaker
 */

public interface BroBookUserRepository extends JpaRepository<BroBookUser, Long> {
    Optional<BroBookUser> findByUsername(String username);
    List<BroBookUser> findByRole(String role);

    boolean existsByUsername(String username);
}

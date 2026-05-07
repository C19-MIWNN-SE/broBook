package nl.miwnn.ch19.binarybros.brobook.repository;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Paul Rademaker
 */

public interface BroBookUserRepository extends JpaRepository<BroBookUser, Long> {
    Optional<BroBookUser> findByUsername(String username);
    List<BroBookUser> findByRole(Role role);

    boolean existsByUsername(String username);
}

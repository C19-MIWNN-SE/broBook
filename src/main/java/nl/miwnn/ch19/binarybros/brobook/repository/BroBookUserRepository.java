package nl.miwnn.ch19.binarybros.brobook.repository;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Paul Rademaker
 */

public interface BroBookUserRepository extends JpaRepository<BroBookUser, Long> {
}

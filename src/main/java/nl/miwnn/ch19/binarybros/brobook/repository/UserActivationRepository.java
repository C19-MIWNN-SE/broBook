package nl.miwnn.ch19.binarybros.brobook.repository;

/*
 * @author Mart Stukje
 * !! Doel van programma
 * */

import nl.miwnn.ch19.binarybros.brobook.model.UserActivation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UserActivationRepository extends JpaRepository<UserActivation, Long> {
    List<UserActivation> findAllByUserIdIn(Collection<Long> userIds);

    boolean existsUserActivationByTokenAndUsedIsFalse(String token);

    boolean existsByToken(String token);

    UserActivation findByToken(String token);
}

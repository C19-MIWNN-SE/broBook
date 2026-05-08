package nl.miwnn.ch19.binarybros.brobook.repository;

import nl.miwnn.ch19.binarybros.brobook.builder.UserActivationTestBuilder;
import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Role;
import nl.miwnn.ch19.binarybros.brobook.model.UserActivation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * @author Mart Stukje
 * */

@DataJpaTest
@ActiveProfiles("test")
class UserActivationRepositoryTest {

    @Autowired
    private UserActivationRepository userActivationRepository;

    @Autowired
    private TestEntityManager entityManager;

    private BroBookUser studentNotActivated;
    private BroBookUser studentActivated;
    private BroBookUser studentExpired;

    @BeforeEach
    void setUp() {
        studentNotActivated = new BroBookUser(
                "JanJanssen", "Jan", "Janssen", "geheim123", Role.STUDENT);
        studentActivated = new BroBookUser(
                "PietPieters", "Piet", "Pieters", "geheim123", Role.STUDENT);
        studentExpired = new BroBookUser(
                "KlaasKlaassen", "Klaas", "Klaassen", "geheim123", Role.STUDENT);

        entityManager.persist(studentNotActivated);
        entityManager.persist(studentActivated);
        entityManager.persist(studentExpired);

        UserActivation userActivation = new UserActivationTestBuilder()
                .token("ABC")
                .build();
        userActivation.setUser(studentNotActivated);

        UserActivation userActivation2 = new UserActivationTestBuilder()
                .token("ABCDE")
                .build();

        UserActivation userActivationExpired = new UserActivationTestBuilder()
                .token("ABCDEFG")
                .expired()
                .build();
        userActivationExpired.setUser(studentExpired);

        UserActivation userActivationToday = new UserActivationTestBuilder()
                .token("TODAY-TOKEN")
                .expireDate(LocalDate.now())
                .build();
        UserActivation userActivationTomorrow = new UserActivationTestBuilder()
                .token("TOMORROW-TOKEN")
                .expireDate(LocalDate.now().plusDays(1))
                .build();

        UserActivation userActivationUsed = new UserActivationTestBuilder()
                .token("USED-TOKEN")
                .used()
                .build();
        userActivationUsed.setUser(studentActivated);


        entityManager.persist(userActivation);
        entityManager.persist(userActivation2);
        entityManager.persist(userActivationExpired);
        entityManager.persist(userActivationToday);
        entityManager.persist(userActivationTomorrow);
        entityManager.persist(userActivationUsed);
        entityManager.flush();
    }

    @ParameterizedTest(name = "{0} returns true")
    @ValueSource(strings = {"ABC", "ABCDEFG"})
    @DisplayName("existsByToken returns true when UserActivation exists")
    void existsByTokenReturnsTrueWhenUserActivationExists(String token) {
        assertTrue(userActivationRepository.existsByToken(token));
    }

    @ParameterizedTest(name = "{0} returns false")
    @NullSource
    @ValueSource(strings = {"ABCD", "123", ""})
    @DisplayName("existsByToken returns false when UserActivation not exists")
    void existsByTokenReturnsFalseWhenUserActivationNotExists(String token) {
        assertFalse(userActivationRepository.existsByToken(token));
    }

    @ParameterizedTest(name = "{0} returns UserActivation object")
    @ValueSource(strings = {"ABC", "ABCDEFG"})
    @DisplayName("findByToken returns correct UserActivation when exists")
    void findByTokenReturnsCorrectUserActivationWhenExists(String token) {
        UserActivation result = userActivationRepository.findByToken(token);

        assertNotNull(result);
        assertEquals(token, result.getToken());
    }
    
    @ParameterizedTest(name = "{0} returns null")
    @NullSource
    @ValueSource(strings = {"ABCD", "123", ""})
    @DisplayName("findByToken returns null when token does not exist")
    void findByTokenReturnsNullWhenTokenDoesNotExist(String token) {
        UserActivation result = userActivationRepository.findByToken(token);

        assertNull(result);
    }

    @ParameterizedTest(name = "{0} returns true")
    @ValueSource(strings = {"ABC", "ABCDE", "ABCDEFG"})
    @DisplayName("existsUserActivationByTokenAndUsedIsFalse returns true when token is not used")
    void existsUserActivationByTokenAndUsedIsFalseReturnsTrueWhenTokenIsNotUsed(String token) {
        assertTrue(userActivationRepository.existsUserActivationByTokenAndUsedIsFalse(token));
    }

    @ParameterizedTest(name = "{0} returns false")
    @ValueSource(strings = {"USED-TOKEN"})
    void existsUserActivationByTokenAndUsedIsFalseReturnsFalseWhenTokenIsUsed(String token) {
        assertFalse(userActivationRepository.existsUserActivationByTokenAndUsedIsFalse(token));
    }

    @ParameterizedTest(name = "{0} returns true")
    @ValueSource(strings = {"ABC", "TODAY-TOKEN", "TOMORROW-TOKEN"})
    @DisplayName("existsUserActivationByTokenAndExpireDateAfter returns true when expire after today")
    void existsUserActivationByTokenAndExpireDateAfterReturnsTrueWhenExpireGreaterThanEqualToday(String token) {
        assertTrue(userActivationRepository.existsUserActivationByTokenAndExpireDateGreaterThanEqual(
                token, LocalDate.now()));
    }

    @ParameterizedTest(name = "{0} returns false")
    @ValueSource(strings = {"ABCDEFG"})
    @DisplayName("existsUserActivationByTokenAndExpireDateAfter returns false when expire before today")
    void existsUserActivationByTokenAndExpireDateAfterReturnsFalseWhenExpireBeforeToday(String token) {
        assertFalse(userActivationRepository.existsUserActivationByTokenAndExpireDateGreaterThanEqual(
                token, LocalDate.now()));
    }

    @Test
    @DisplayName("findAllByUserIdIn returns all UserActivations for given users")
    void findAllByUserIdInReturnsAllUserActivationsForGivenUsers() {
        List<UserActivation> result = userActivationRepository.findAllByUserIdIn(
                List.of(studentNotActivated.getId(), studentExpired.getId()));
        assertEquals(2, result.size());
    }
}
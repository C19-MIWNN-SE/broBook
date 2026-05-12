package nl.miwnn.ch19.binarybros.brobook.service;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.model.Role;
import nl.miwnn.ch19.binarybros.brobook.repository.CohortRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
/*
 * @author Mart Stukje
 * */

@ExtendWith(MockitoExtension.class)
class CohortServiceTest {

    @Mock
    private CohortRepository cohortRepository;

    @InjectMocks
    private CohortService cohortService;

    private BroBookUser user;
    private Cohort cohort;

    @BeforeEach
    void setUp() {
        user = new BroBookUser();
        cohort = new Cohort("Cohort 1", "Software Engineering", LocalDate.now());
    }

    @Test
    @DisplayName("saveCohort should save cohort with teacher in it when teacher creates")
    void saveCohortShouldSaveCohortWithTeacherInItWhenTeacherCreates() {
        user.setRole(Role.TEACHER);

        cohortService.saveCohort(cohort, user);

        assertTrue(cohort.getParticipants().contains(user));
        verify(cohortRepository, times(1)).save(cohort);
    }

    @Test
    @DisplayName("saveCohort should save cohort without user in it when admin creates")
    void saveCohortShouldSaveCohortWithoutUserInItWhenAdminCreates() {
        user.setRole(Role.ADMIN);

        cohortService.saveCohort(cohort, user);

        assertFalse(cohort.getParticipants().contains(user));
        verify(cohortRepository, times(1)).save(cohort);
    }
}
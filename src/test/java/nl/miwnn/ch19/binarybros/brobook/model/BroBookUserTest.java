package nl.miwnn.ch19.binarybros.brobook.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


class BroBookUserTest {

    private BroBookUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new BroBookUser();
        testUser.setFirstName("Paul");
        testUser.setLastName("Rademaker");
    }

    @Test
    void testGetFullName() {
        String fullName = testUser.getFullName();

        assertThat(fullName).isEqualTo("Paul Rademaker");
    }

    @Test
    void testRoleAssignment() {
        testUser.setRole(Role.STUDENT);

        assertThat(testUser.getRole()).isEqualTo(Role.STUDENT);
        assertThat(testUser.getRole().name()).isEqualTo("STUDENT");
    }

    @Test
    void testCohortsListIsInitialized() {
        assertThat(testUser.getCohorts()).isNotNull();
        assertThat(testUser.getCohorts()).isEmpty();
    }

    @Test
    void testAddCohortToUser() {
        Cohort cohort = new Cohort();
        cohort.setName("C19");

        testUser.getCohorts().add(cohort);

        assertThat(testUser.getCohorts()).hasSize(1);
        assertThat(testUser.getCohorts().get(0).getName()).isEqualTo("C19");
    }
}
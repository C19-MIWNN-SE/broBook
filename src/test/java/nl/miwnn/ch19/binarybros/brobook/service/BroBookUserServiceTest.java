package nl.miwnn.ch19.binarybros.brobook.service;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.model.Role;
import nl.miwnn.ch19.binarybros.brobook.repository.BroBookUserRepository;
import nl.miwnn.ch19.binarybros.brobook.repository.CohortRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BroBookUserServiceTest {

    @Mock
    private BroBookUserRepository userRepository;

    @Mock
    private CohortRepository cohortRepository;

    @InjectMocks
    private BroBookUserService userService;

    @Test
    void findVisibleUsers_AsAdmin_ShouldReturnAllUsers() {
        BroBookUser admin = new BroBookUser();
        admin.setRole(Role.ADMIN);

        List<BroBookUser> allUsers = List.of(new BroBookUser(), new BroBookUser(), new BroBookUser());
        when(userRepository.findAll()).thenReturn(allUsers);

        List<BroBookUser> result = userService.findVisibleUsers(admin);

        assertThat(result).hasSize(3);
        verify(userRepository).findAll();
    }

    @Test
    void findVisibleUsers_AsStudent_ShouldReturnOnlyCohortMatesAndTeachers() {
        BroBookUser student = new BroBookUser();
        student.setRole(Role.STUDENT);
        student.setFirstName("Paul");

        Cohort cohort = new Cohort();
        BroBookUser classmate = new BroBookUser();
        classmate.setFirstName("Mart");
        cohort.setParticipants(List.of(student, classmate));
        student.setCohorts(List.of(cohort));

        BroBookUser teacher = new BroBookUser();
        teacher.setRole(Role.TEACHER);

        when(userRepository.findByRole(Role.TEACHER)).thenReturn(List.of(teacher));

        List<BroBookUser> result = userService.findVisibleUsers(student);

        assertThat(result).containsExactlyInAnyOrder(student, classmate, teacher);
    }
}
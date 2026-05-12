package nl.miwnn.ch19.binarybros.brobook.service;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.UserActivation;
import nl.miwnn.ch19.binarybros.brobook.repository.BroBookUserRepository;
import nl.miwnn.ch19.binarybros.brobook.repository.UserActivationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/*
 * @author Mart Stukje
 * */

@ExtendWith(MockitoExtension.class)
class UserActivationServiceTest {

    @Mock
    private UserActivationRepository userActivationRepository;

    @Mock
    private BroBookUserRepository broBookUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserActivationService userActivationService;

    @Test
    @DisplayName("generateActivation should generate activation, save and return")
    void generateActivationShouldGenerateActivationSaveAndReturn() {
        //Arrange
        BroBookUser user = new BroBookUser();

        when(userActivationRepository.save(any(UserActivation.class)))
                .thenAnswer(call -> call.getArgument(0));
        when(userActivationRepository.existsByToken(anyString())).thenReturn(false);

        //Act
        UserActivation userActivation= userActivationService.generateActivation(user);

        //Assert
        assertNotNull(userActivation);
        assertEquals(user, userActivation.getUser());
        assertEquals(LocalDate.now().plusDays(7), userActivation.getExpireDate());
        verify(userActivationRepository, times(1)).save(userActivation);
    }

    @Test
    @DisplayName("generateActivation should retry GenerateUniqueToken when collision")
    void generateActivationShouldRetryGenerateUniqueTokenWhenCollision() {
        //Arrange
        when(userActivationRepository.existsByToken(anyString()))
                .thenReturn(true)
                .thenReturn(false);

        //Act
        userActivationService.generateActivation(new BroBookUser());

        //Assert
        verify(userActivationRepository, times(2)).existsByToken(anyString());
    }

    @Test
    @DisplayName("savePasswordWithToken should set activation to used")
    void savePasswordWithTokenShouldSetActivationToUsed() {
        //Arrange
        BroBookUser user = new BroBookUser();
        UserActivation userActivation = new UserActivation();
        userActivation.setToken("ABC");
        userActivation.setUser(user);

        when(userActivationRepository.findByToken("ABC")).thenReturn(userActivation);

        //Act
        userActivationService.savePasswordWithToken("geheim123", "ABC");

        //Assert
        assertTrue(userActivation.isUsed());
    }

    @Test
    @DisplayName("savePasswordWithToken should encode password and save user")
    void savePasswordWithTokenShouldEncodePasswordAndSaveUser() {
        BroBookUser user = new BroBookUser();
        UserActivation userActivation = new UserActivation();
        userActivation.setToken("ABC");
        userActivation.setUser(user);

        when(userActivationRepository.findByToken("ABC")).thenReturn(userActivation);
        when(passwordEncoder.encode("geheim123")).thenReturn("encoded_password");

        userActivationService.savePasswordWithToken("geheim123", "ABC");

        assertNotEquals("geheim123", user.getPassword());
        verify(broBookUserRepository, times(1)).save(user);
    }
}
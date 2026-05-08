package nl.miwnn.ch19.binarybros.brobook.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/*
 * @author Mart Stukje
 * */

class UserActivationTest {

    @Test
    @DisplayName("isUsed returns false for a new generated UserActivation")
    void isUsedReturnsFalseForANewGeneratedUserActivation() {
        UserActivation userActivation = new UserActivation();
        assertFalse(userActivation.isUsed());
    }
}
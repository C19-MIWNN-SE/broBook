package nl.miwnn.ch19.binarybros.brobook.builder;

/*
 * @author Mart Stukje
 * Builds UserActivations needed for testing
 * */

import nl.miwnn.ch19.binarybros.brobook.model.UserActivation;

import java.time.LocalDate;

public class UserActivationTestBuilder {
    private String token = "default-token";
    private LocalDate expireDate = LocalDate.now().plusDays(7);
    private boolean used = false;

    public UserActivationTestBuilder token(String token){
        this.token = token;
        return this;
    }

    public UserActivationTestBuilder expireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
        return this;
    }

    public UserActivationTestBuilder used() {
        this.used = true;
        return this;
    }

    public UserActivationTestBuilder expired() {
        return expireDate(LocalDate.now().minusDays(1));
    }

    public UserActivation build() {
        UserActivation userActivation = new UserActivation();
        userActivation.setToken(token);
        userActivation.setExpireDate(expireDate);
        userActivation.setUsed(used);
        return userActivation;
    }
}

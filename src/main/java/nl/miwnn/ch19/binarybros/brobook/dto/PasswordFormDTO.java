package nl.miwnn.ch19.binarybros.brobook.dto;

/*
 * @author Mart Stukje
 * Supports the password form
 * */

import jakarta.validation.constraints.NotBlank;

public class PasswordFormDTO {
    @NotBlank(message = "Het wachtwoord mag niet leeg zijn")
    String plainPassword;

    @NotBlank(message = "Vul ter controle twee keer je wachtwoord in")
    String checkPassword;

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }
}

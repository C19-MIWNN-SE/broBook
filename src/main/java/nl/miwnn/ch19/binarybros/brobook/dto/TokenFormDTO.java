package nl.miwnn.ch19.binarybros.brobook.dto;

/*
 * @author Mart Stukje
 * !! Doel van programma
 * */

import jakarta.validation.constraints.NotBlank;

public class TokenFormDTO {
    @NotBlank(message = "De activatiecode mag niet leeg zijn")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

package nl.miwnn.ch19.binarybros.brobook.dto;

/*
 * @author Mart Stukje
 * Supports the new user form
 * */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserAccountFormDTO extends BaseUserFormDTO {
    @NotBlank(message = "Een gebruikersnaam is verplicht voor elke gebruiker")
    private String username;

    @NotNull(message = "Vul de rol voor deze gebruiker in.")
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

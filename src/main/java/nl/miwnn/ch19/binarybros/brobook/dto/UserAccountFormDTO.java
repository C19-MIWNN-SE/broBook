package nl.miwnn.ch19.binarybros.brobook.dto;

/*
 * @author Mart Stukje
 * Supports the new user form
 * */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.miwnn.ch19.binarybros.brobook.model.Role;

public class UserAccountFormDTO extends BaseUserFormDTO {
    @NotBlank(message = "Een gebruikersnaam is verplicht voor elke gebruiker")
    private String username;

    @NotNull(message = "Vul de rol voor deze gebruiker in.")
    private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

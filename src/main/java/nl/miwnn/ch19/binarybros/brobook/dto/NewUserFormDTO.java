package nl.miwnn.ch19.binarybros.brobook.dto;

/*
 * @author Mart Stukje
 * Supports the new user form
 * */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;

import java.time.LocalDate;
import java.util.List;

public class NewUserFormDTO {

    @NotBlank(message = "Een gebruikersnaam is verplicht voor elke gebruiker")
    private String username;

    @NotBlank(message = "Een voornaam is verplicht voor elke gebruiker.")
    private String firstName;

    @NotBlank(message = "Een achternaam is verplicht voor elke gebruiker.")
    private String lastName;

    @NotNull(message = "Vul de rol voor deze gebruiker in.")
    private String role;

    private String futureEmployer;
    private LocalDate birthDate;
    private List<Cohort> cohorts;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFutureEmployer() {
        return futureEmployer;
    }

    public void setFutureEmployer(String futureEmployer) {
        this.futureEmployer = futureEmployer;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Cohort> getCohorts() {
        return cohorts;
    }

    public void setCohorts(List<Cohort> cohorts) {
        this.cohorts = cohorts;
    }
}

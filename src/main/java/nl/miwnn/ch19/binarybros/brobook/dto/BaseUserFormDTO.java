package nl.miwnn.ch19.binarybros.brobook.dto;

/*
 * @author Mart Stukje
 * Lays a foundation for DTO's regarding the BroBookUser object
 * */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public abstract class BaseUserFormDTO {

    @NotBlank(message = "Een voornaam is verplicht voor elke gebruiker.")
    private String firstName;

    @NotBlank(message = "Een achternaam is verplicht voor elke gebruiker.")
    private String lastName;

    private String futureEmployer;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    private List<Long> cohortIds;

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

    public List<Long> getCohortIds() {
        return cohortIds;
    }

    public void setCohortIds(List<Long> cohortIds) {
        this.cohortIds = cohortIds;
    }
}

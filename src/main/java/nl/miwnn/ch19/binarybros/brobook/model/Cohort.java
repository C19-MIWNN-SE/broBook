package nl.miwnn.ch19.binarybros.brobook.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Rademaker
 * Cohorts of Make IT Work of which multiple students/teachers can be part of
 */

@Entity
public class Cohort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate startDate;
    private String subject;
    @ManyToMany
    private List<BroBookUser> participants = new ArrayList<>();

    public Cohort(String name, String subject, LocalDate startDate) {
        this.name = name;
        this.subject = subject;
        this.startDate = startDate;
    }

    public Cohort() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<BroBookUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<BroBookUser> participants) {
        this.participants = participants;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}

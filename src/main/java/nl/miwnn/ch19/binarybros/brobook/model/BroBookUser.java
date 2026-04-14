package nl.miwnn.ch19.binarybros.brobook.model;

import jakarta.persistence.*;

@Entity
@Table(name = "BroBookUsers")
public class BroBookUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private String futureEmployer;
    private String Role;
    @Column(columnDefinition = "TEXT")
    private String bio;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image profilePicture;


    public BroBookUser() {}

    public BroBookUser(String firstName, String lastName, Integer birthYear, String futureEmployer, String bio) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.futureEmployer = futureEmployer;
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return firstName + " " + lastName;
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

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getFutureEmployer() {
        return futureEmployer;
    }

    public void setFutureEmployer(String futureEmployer) {
        this.futureEmployer = futureEmployer;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public Image getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }
}
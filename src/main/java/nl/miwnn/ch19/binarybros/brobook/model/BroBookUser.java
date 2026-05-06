package nl.miwnn.ch19.binarybros.brobook.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "BroBookUsers")
public class BroBookUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CsvBindByName(column = "gebruikersnaam")
    @NotBlank(message = "Een gebruikersnaam is verplicht voor elke gebruiker")
    @Column(unique = true)
    private String username;

    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserActivation userActivation;

    @CsvBindByName(column = "rol")
    @NotNull(message = "Vul de rol voor deze gebruiker in.")
    private String role;

    @CsvBindByName(column = "voornaam")
    @NotBlank(message = "Een voornaam is verplicht voor elke gebruiker.")
    @Column(nullable = false)
    private String firstName;

    @CsvBindByName(column = "achternaam")
    @NotBlank(message = "Een achternaam is verplicht voor elke gebruiker.")
    private String lastName;

    private String residence;

    @CsvBindByName(column = "geboortedatum")
    @CsvDate("yyyy-MM-dd")
    private LocalDate birthDate;

    @CsvBindByName(column = "werkgever")
    private String futureEmployer;

    @CsvBindByName(column = "bio")
    @Column(columnDefinition = "TEXT")
    private String bio;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "image_id")
    private Image profilePicture;

    @ManyToMany
    @JoinTable(
            name = "cohort_participants",
            joinColumns = @JoinColumn(name = "participants_id"),
            inverseJoinColumns = @JoinColumn(name = "cohort_id")
    )
    private List<Cohort> cohorts = new ArrayList<>();

    public BroBookUser() {}

    public BroBookUser(String username, String firstName, String lastName, String password, String role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserActivation getUserActivation() {
        return userActivation;
    }

    public void setUserActivation(UserActivation userActivation) {
        this.userActivation = userActivation;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthYear) {
        this.birthDate = birthYear;
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
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Image getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<Cohort> getCohorts() {
        return cohorts;
    }

    public void setCohorts(List<Cohort> cohorts) {
        this.cohorts = cohorts;
    }
}
package nl.miwnn.ch19.binarybros.brobook.model;

import jakarta.persistence.*;
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

    private String username;
    private String password;
    private String role;
    @Column(nullable = false)
    private String firstName;

    private String lastName;
    private LocalDate birthDate;
    private String futureEmployer;

    @Column(columnDefinition = "TEXT")
    private String bio;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image profilePicture;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "cohort_participants",
            joinColumns = @JoinColumn(name = "participants_id"),
            inverseJoinColumns = @JoinColumn(name = "cohort_id")
    )
    private List<Cohort> cohorts = new ArrayList<>();

    public BroBookUser() {}

    public BroBookUser(String firstName, String lastName, String futureEmployer, LocalDate birthDate, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.futureEmployer = futureEmployer;
        this.birthDate = birthDate;
        this.role = role;
    }

    public BroBookUser(String firstName, String lastName, String password, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        setUsername();
        this.password = password;
        this.role = role;
    }

    public BroBookUser(String firstName, String lastName, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    private void setUsername() {
        this.username = toTitleCase(firstName) + toTitleCase(lastName);
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

    private String toTitleCase(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
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
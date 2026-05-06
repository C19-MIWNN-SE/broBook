package nl.miwnn.ch19.binarybros.brobook.model;

/*
 * @author Mart Stukje
 * The information a user needs to activate it's broBook account
 * */

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class UserActivation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private BroBookUser user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDate expireDate;

    @Column(nullable = false)
    private boolean used = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BroBookUser getUser() {
        return user;
    }

    public void setUser(BroBookUser user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}

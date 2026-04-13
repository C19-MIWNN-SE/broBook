package nl.miwnn.ch19.binarybros.brobook.model;

/*
 * @author Mart Stukje
 * !! Doel van programma
 * */

public class BroBookUser {

    private String firstName;
    private String lastName;
    private String role;

    public BroBookUser(String firstName, String lastName, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
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
}

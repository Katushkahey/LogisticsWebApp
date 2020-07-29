package com.tsystems.entities;


import com.tsystems.entities.enums.Role;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "authority_id")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user")
    private Driver driver;

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        User user = (User) o;
        return ((User) o).id == id &&
                ((User) o).login == login &&
                ((User) o).role == role;
    }

    @Override
    public int hashCode() {
        return 53 * login.hashCode() +
                61 * id.hashCode() +
                73 * role.hashCode();
    }

    @Override
    public String toString() {
        return "User: " +
                "id = " + id +
                ", login = " + login +
                ", password = " + password +
                ", role= " + role;
    }
}

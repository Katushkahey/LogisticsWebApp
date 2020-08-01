package com.tsystems.logisticsProject.entity;


import com.tsystems.logisticsProject.entity.enums.Authority;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @NotEmpty
    @Column(name = "username")
    private String username;

    @NonNull
    @NotEmpty
    @Column(name = "password")
    private String password;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToOne(mappedBy = "user")
    private Driver driver;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}

package com.tsystems.logisticsProject.entity;

import com.tsystems.logisticsProject.entity.Authority;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "users")
public class User extends AbstractEntity {

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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="authority_id")
    private Authority authority;

    @OneToOne(mappedBy = "user")
    private Driver driver;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username +
                "role = " + authority.toString() +'\'' +
                '}';
    }
}

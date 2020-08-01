package com.tsystems.logisticsProject.entity;


import com.tsystems.logisticsProject.entity.enums.Authority;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @NotEmpty
    @Column(name = "login")
    private String login;

    @NonNull
    @NotEmpty
    @Column(name = "password")
    private String password;

    @NonNull
    @Column(name = "authority_id")
    private String authority;

    @OneToOne(mappedBy = "user")
    private Driver driver;

}

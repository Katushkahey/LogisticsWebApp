package com.tsystems.logisticsProject.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Table(name = "authorities")
public class Role extends AbstractEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "authority")
    private String authority;

    @Transient
    @OneToOne(mappedBy = "authority")
    private Set<User> users;

    public Role(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public Role(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}

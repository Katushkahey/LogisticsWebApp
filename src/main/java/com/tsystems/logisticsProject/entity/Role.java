package com.tsystems.logisticsProject.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Table(name = "authorities")
public class Role extends AbstractEntity implements GrantedAuthority {

    @Column(name = "authority")
    private String authority;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "authority")
    private List<User> users;

    @Override
    public String getAuthority() {
        return authority;
    }
}

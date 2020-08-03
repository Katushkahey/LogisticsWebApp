package com.tsystems.logisticsProject.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyUserDetailService implements UserDetailsService {

    private Map<String, User> roles = new HashMap<>();

    @PostConstruct
    public void init() {
        roles.put("admin", new User("admin",
                "$2a$10$B3zf9cF88Rd6y6ZVmB13COplMDsXtoEnsfr6sAYlcj2oYG1eQ89qy",
                getAuthority("ROLE_ADMIN")));
        roles.put("driver", new User("driver",
                "$2a$10$H1J40MNH/xUWzX4zGEvltOIAIgd1MACNOnNK8ShWK/oLMme2XBphe",
                getAuthority("ROLE_DRIVER")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return roles.get(username);
    }

    private List<GrantedAuthority> getAuthority(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
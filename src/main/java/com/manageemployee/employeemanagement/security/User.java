package com.manageemployee.employeemanagement.security;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "UZER")
@NoArgsConstructor
public class User implements UserDetails {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "UZER_NAME")
    @NotNull
    @Size(min = 3, max = 40)
    private String userName;

    @Column(name = "PASSWORD")
    @NotNull
    @Size(min = 1, max = 255)
    private String password;

    @ElementCollection
    @Setter
    private Set<UserRole> roles;

    @Column(name = "IS_ENABLED", nullable = false)
    @Setter
    private boolean isEnabled;

    public void addRole(UserRole role) {
        roles.add(role);
    }

    public void removeRole(UserRole role) {
        roles.remove(role);
    }
    public void clearRoles() {
        roles.clear();
    }

    private User(String userName, String password, Set<UserRole> roles) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    public static User createUser(String userName, Set<UserRole> roles, String password) {
        User user = new User(userName, password, roles);
        user.setEnabled(true);
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}

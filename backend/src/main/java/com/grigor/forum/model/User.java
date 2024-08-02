package com.grigor.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.grigor.forum.validators.XSSValid;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Data
@Builder
@EqualsAndHashCode(callSuper = false, exclude = "permissions")
@ToString(exclude = "permissions")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "username", unique = true)
    @NotBlank
    @XSSValid
    private String username;

    @Basic
    @Column(name = "password")
    @NotBlank
    @XSSValid
    private String password;

    @Basic
    @Column(name = "email")
    @NotBlank
    @XSSValid
    private String email;

    @Basic
    @Column(name = "role")
    @NotBlank
    @XSSValid
    private String role;

    @Basic
    @Column(name = "verified")
    private boolean verified;

    @Basic
    @Column(name = "banned")
    private boolean banned;

    @Basic
    @Column(name = "code")
    @NotNull
    private int code;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference("permission-user")
    @NotNull
    private List<Permission> permissions;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = permissions.stream()
                .flatMap(permission -> Stream.of(
                        new SimpleGrantedAuthority(permission.isPost() ? "post" : "no"),
                        new SimpleGrantedAuthority(permission.isDelete() ? "delete" : "no"),
                        new SimpleGrantedAuthority(permission.isEdit() ? "edit" : "no")
                ))
                .collect(Collectors.toList());

        //authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        authorities.add(new SimpleGrantedAuthority(role));

        return authorities.stream()
                .filter(auth -> !auth.getAuthority().isEmpty())
                .collect(Collectors.toList());
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
        return true;
    }
}


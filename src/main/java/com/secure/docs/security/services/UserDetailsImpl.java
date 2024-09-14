package com.secure.docs.security.services;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.secure.docs.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

//adapts the User.java model to custom userDetailsImpl of userDetails interface for spring security impls
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID =1L;
    private Long id;
    private String username;
    private String email;

    @JsonIgnore
    private String password;
    private boolean is2faEnabled;

    private Collection<? extends GrantedAuthority> authorities;



    //maps user roles and other attributes to UserDetailsImpl
    public static UserDetailsImpl build(User user){
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());

        return new UserDetailsImpl(
                user.getUserId(),
                user.getUserName(),
                user.getPassword(),
                user.getEmail(),
                user.isTwoFactorEnabled(),
                List.of(authority)
        );
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
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

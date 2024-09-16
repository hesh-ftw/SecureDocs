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
import java.util.Objects;

@Data
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
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName().name());

        return new UserDetailsImpl(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                user.isTwoFactorEnabled(),
                List.of(authority)
        );
    }

    public UserDetailsImpl(Long id, String username, String email, String password, boolean is2faEnabled, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.is2faEnabled = is2faEnabled;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    public Long getId(){
        return id;
    }
    public String getEmail(){
        return email;
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

    public boolean is2faEnabled(){
        return is2faEnabled;
    }

    @Override
    public boolean equals(Object o){
        if(this== o)
            return true;
        if( o==null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}

package com.tianxue.boot.user;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@ToString
public class UserInfo implements UserDetails
{
    private String password;

    private String username;

    @Override
    public String toString() {
        return username+password;
    }

    private boolean lock;

    public  Collection<? extends GrantedAuthority>  authorities;

    public UserInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserInfo setLock(boolean lock) {
        this.lock = lock;
        return this;
    }

    public UserInfo setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public UserInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return lock;
    }

    @Override
    public boolean isAccountNonLocked() {
        return lock;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return lock;
    }

    @Override
    public boolean isEnabled() {
        return lock;
    }
}

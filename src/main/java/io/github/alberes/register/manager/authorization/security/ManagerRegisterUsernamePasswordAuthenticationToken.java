package io.github.alberes.register.manager.authorization.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ManagerRegisterUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public ManagerRegisterUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public ManagerRegisterUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}

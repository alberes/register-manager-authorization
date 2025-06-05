package io.github.alberes.register.manager.authorization.security;

import io.github.alberes.register.manager.authorization.constants.Constants;
import io.github.alberes.register.manager.authorization.domains.UserAccount;
import io.github.alberes.register.manager.authorization.services.UserService;
import io.github.alberes.register.manager.authorization.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagerRegisterAuthenticationProvider implements AuthenticationProvider {

    private final UserService service;

    private final PasswordEncoder encoder;

    private final ControllerUtils controllerUtils;

    @Override
    public UsernamePasswordAuthenticationToken authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserAccount userAccount = this.service.loadUserByUsername(login);
        if(userAccount == null){
            throw new UsernameNotFoundException(Constants.USER_OR_PASSWORD_INCORRECT);
        }

        if(this.encoder.matches(password, userAccount.getPassword())){
            ManagerRegisterUsernamePasswordAuthenticationToken authenticationToken =
                    new ManagerRegisterUsernamePasswordAuthenticationToken(userAccount, null,
                            this.controllerUtils.toAuthorities(userAccount.getRoles()));
            return authenticationToken;
        }

        throw new UsernameNotFoundException(Constants.USER_OR_PASSWORD_INCORRECT);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(ManagerRegisterUsernamePasswordAuthenticationToken.class);
    }
}

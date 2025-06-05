package io.github.alberes.register.manager.authorization.services;

import io.github.alberes.register.manager.authorization.constants.Constants;
import io.github.alberes.register.manager.authorization.controllers.dto.LoginDto;
import io.github.alberes.register.manager.authorization.controllers.dto.TokenDto;
import io.github.alberes.register.manager.authorization.domains.UserAccount;
import io.github.alberes.register.manager.authorization.repositories.UserAccountRepository;
import io.github.alberes.register.manager.authorization.services.exceptions.AuthorizationException;
import io.github.alberes.register.manager.authorization.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final AuthenticationProvider provider;

    private final JWTService service;

    private final ControllerUtils controllerUtils = new ControllerUtils();

    public TokenDto verify(LoginDto dto, List<String> origins) throws Exception {

        Authentication authentication =
                this.provider.authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        if(authentication.isAuthenticated()){
            UserAccount userAccount = (UserAccount)authentication.getPrincipal();
            if(this.controllerUtils.hasRoleAdmin(userAccount.getRoles())) {
                origins.add(userAccount.getId().toString());
            }else{
                origins.add(userAccount.getId().toString());
            }
            return this.service.generateToken(userAccount, origins);
        }else{
            AuthorizationException authorizationException = new AuthorizationException(Constants.AUTHORIZATION_FAILURE);
            log.error(Constants.AUTHORIZATION_FAILURE_USER + dto.username(), authorizationException);
            throw authorizationException;
        }
    }

}

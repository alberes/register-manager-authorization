package io.github.alberes.register.manager.authorization.services;

import io.github.alberes.register.manager.authorization.constants.Constants;
import io.github.alberes.register.manager.authorization.domains.UserAccount;
import io.github.alberes.register.manager.authorization.repositories.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserAccountRepository repository;

    @Transactional
    public UserAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = this.repository.findByEmail(username);
        if(userAccount == null){
            UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException(Constants.OBJECT_NOT_FOUND);
            log.error(usernameNotFoundException.getMessage(), usernameNotFoundException);
            throw usernameNotFoundException;
        }
        userAccount.getRoles().size();
        return userAccount;
    }
}
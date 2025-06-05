package io.github.alberes.register.manager.authorization.services;

import io.github.alberes.register.manager.authorization.constants.Constants;
import io.github.alberes.register.manager.authorization.domains.UserAccount;
import io.github.alberes.register.manager.authorization.repositories.UserAccountRepository;
import io.github.alberes.register.manager.authorization.utils.ControllerUtils;
import io.github.alberes.register.manager.authorization.services.exceptions.DuplicateRecordException;
import io.github.alberes.register.manager.authorization.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountService{

    private final UserAccountRepository repository;

    private final PasswordEncoder encoder;

    private final ControllerUtils controllerUtils;

    @Transactional
    @Modifying
    public UserAccount save(UserAccount userAccount){
        UserAccount userAccountDB = this.repository.findByEmail(userAccount.getEmail());
        if(userAccountDB != null){
            DuplicateRecordException duplicateRecordException = new DuplicateRecordException(
                    Constants.REGISTRATION_WITH_E_MAIL + userAccount.getEmail() + Constants.HAS_ALREADY_BEEN_REGISTERED);
            log.error(duplicateRecordException.getMessage(), duplicateRecordException);
            throw duplicateRecordException;
        }
        String password = this.encoder.encode(userAccount.getPassword());
        userAccount.setPassword(password);
        userAccount = this.repository.save(userAccount);
        log.info("New user saved, userId: {}", userAccount.getId());
        return userAccount;
    }

    @Transactional
    public UserAccount find(UUID id){
        Optional<UserAccount> optional = this.repository.findById(id);
        log.info("User {}found. UserId: {}", (optional.isPresent()? "" : "not "), id);
        return optional.orElseThrow(() -> new ObjectNotFoundException(
                Constants.OBJECT_NOT_FOUND_ID + id.toString() + Constants.TYPE + UserAccount.class.getName()));
    }

    @Transactional
    @Modifying
    public void update(UserAccount userAccount){
        UserAccount userAccountDB = this.find(userAccount.getId());
        userAccountDB.setName(userAccount.getName());
        this.repository.save(userAccountDB);
        log.info("Updated user {}", userAccountDB.getId());
    }

    public void delete(UUID id){
        this.find(id);
        this.repository.deleteById(id);
        log.info("User deleted userId: {}", id);
    }

    @Transactional
    public Page<UserAccount> findPage(UUID id, Integer page, Integer linesPerPage, String orderBy, String direction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount)authentication.getPrincipal();
        Page<UserAccount> users = null;
        if(this.controllerUtils.hasRoleAdmin(userAccount.getRoles())) {
            users = this.repository.findAll(
                    PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy));
            log.info("Users(ADMIN) - total page: {}, total elements: {}", users.getTotalPages(), users.getTotalElements());
            return users;
        }else{
            users = this.repository.findById(id, PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy));
            log.info("Users(USER) - UserId: {}, total page: {}, total elements: {}", id, users.getTotalPages(), users.getTotalElements());
            return users;
        }
    }

    @Transactional
    public boolean notExistsEmail(String email){
        UserAccount userAccount = this.repository.findByEmail(email);
        return userAccount == null;
    }

}

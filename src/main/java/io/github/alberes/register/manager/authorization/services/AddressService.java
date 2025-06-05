package io.github.alberes.register.manager.authorization.services;

import io.github.alberes.register.manager.authorization.domains.Address;
import io.github.alberes.register.manager.authorization.domains.UserAccount;
import io.github.alberes.register.manager.authorization.repositories.AddressRepository;
import io.github.alberes.register.manager.authorization.services.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService{

    private final AddressRepository repository;

    private final UserAccountService userAccountService;

    @Transactional
    @Modifying
    public Address save(Address address){
        UserAccount userAccount = this.userAccountService.find(address.getUserAccount().getId());
        address.setUserAccount(userAccount);
        address = this.repository.save(address);
        log.info("New address saved to user: {}, addressId: {}", userAccount.getId(), address.getId());
        return address;
    }

    @Transactional
    public Address find(UUID userId, UUID id){
        this.userAccountService.find(userId);
        Optional<Address> optional = this.repository.findById(id);
        log.info("Address {}found. UserId: {}, addressId: {}", (optional.isPresent()? "" : "not "), userId, id);
        return optional.orElseThrow(()-> new ObjectNotFoundException(
                "Object not found! Id: " + id.toString() + ", Type: " + Address.class.getName()
        ));
    }

    @Transactional
    @Modifying
    public void update(Address address){
        Address addressDB = this.find(address.getUserAccount().getId(), address.getId());
        address.setUserAccount(addressDB.getUserAccount());
        address.setCreatedDate(addressDB.getCreatedDate());
        this.repository.save(address);
        log.info("Updated address {}", address.getId());
    }

    @Transactional
    @Modifying
    public void delete(UUID userId, UUID id){
        this.find(userId, id);
        this.repository.deleteById(id);
        log.info("Address deleted userId: {}, addressId: {}", userId, id);
    }

    @Transactional
    public Page<Address> findPage(UUID userId, Integer page, Integer linesPerPage, String orderBy, String direction) {
        this.userAccountService.find(userId);
        Page<Address> addresses = this.repository.findByUserAccountId(userId, PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy));
        log.info("Addresses - UserId: {}, total page: {}, total elements: {}", userId, addresses.getTotalPages(), addresses.getTotalElements());
        return addresses;
    }

}

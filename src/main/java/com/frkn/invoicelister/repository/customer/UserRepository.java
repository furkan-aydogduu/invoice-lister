package com.frkn.invoicelister.repository.customer;

import com.frkn.invoicelister.model.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<SystemUser, Long> {
    SystemUser findByName(String name);
}

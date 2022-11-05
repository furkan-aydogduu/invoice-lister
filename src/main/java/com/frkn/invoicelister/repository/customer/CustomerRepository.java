package com.frkn.invoicelister.repository.customer;

import com.frkn.invoicelister.ws.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
package com.frkn.invoicelister.ws.validation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WSValidation {

    Boolean validate();

    Boolean validateBy(JpaRepository repository);

}

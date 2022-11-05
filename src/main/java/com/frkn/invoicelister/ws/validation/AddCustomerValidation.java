package com.frkn.invoicelister.ws.validation;

import com.frkn.invoicelister.ws.AddCustomerRequest;
import com.frkn.invoicelister.ws.model.WSRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public class AddCustomerValidation implements WSValidation{

    private WSRequest request;
    public AddCustomerValidation(WSRequest request) {
        this.request = request;
    }

    @Override
    public Boolean validate() {
        AddCustomerRequest _request = (AddCustomerRequest) this.request;

        if(_request.getName() == null || _request.getName().isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public Boolean validateBy(JpaRepository repository) {
        return true;
    }

}

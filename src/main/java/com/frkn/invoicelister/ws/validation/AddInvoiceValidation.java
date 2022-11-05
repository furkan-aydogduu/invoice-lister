package com.frkn.invoicelister.ws.validation;

import com.frkn.invoicelister.repository.customer.CustomerRepository;
import com.frkn.invoicelister.ws.AddInvoiceRequest;
import com.frkn.invoicelister.ws.Customer;
import com.frkn.invoicelister.ws.model.WSRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public class AddInvoiceValidation implements WSValidation{

    private WSRequest request;

    public AddInvoiceValidation(WSRequest request) {
        this.request = request;
    }

    @Override
    public Boolean validate() {
        AddInvoiceRequest _request = (AddInvoiceRequest) this.request;

        if(_request.getInvoicedate() == null || !_request.getInvoicedate().isValid()){
            return false;
        }
        else if(_request.getPrice() == null || _request.getPrice().isNaN() || _request.getPrice() < 0){
            return false;
        }
        else if(_request.getCustomerId() == null || _request.getCustomerId() < 0){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public Boolean validateBy(JpaRepository repository) {
        AddInvoiceRequest _request = (AddInvoiceRequest) this.request;

        if(repository instanceof CustomerRepository){
            Optional<Customer> customer = ((CustomerRepository) repository).findById(_request.getCustomerId());

            if(customer.isPresent()){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

}

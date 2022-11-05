package com.frkn.invoicelister.ws.validation;

import com.frkn.invoicelister.ws.GetDataRequest;
import com.frkn.invoicelister.ws.model.WSRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public class GetDataRequestValidation implements WSValidation{

    private WSRequest request;
    public GetDataRequestValidation(WSRequest request) {
        this.request = request;
    }

    @Override
    public Boolean validate() {
        GetDataRequest _request = (GetDataRequest) this.request;

        if(_request.getListType() == null || (_request.getListType() != 0 && _request.getListType() != 1)){
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

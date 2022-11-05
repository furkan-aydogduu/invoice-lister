package com.frkn.invoicelister.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceStatus", propOrder = {
        "code",
        "message"
})
public class ServiceStatus {

    private String code;
    private String message;

    public ServiceStatus() {
    }

    public ServiceStatus(String code, String description) {
        this.code = code;
        this.message = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

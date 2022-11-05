package com.frkn.invoicelister.endpoint;

import com.frkn.invoicelister.repository.customer.CustomerRepository;
import com.frkn.invoicelister.repository.invoice.InvoiceRepositoryAdapter;
import com.frkn.invoicelister.ws.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Endpoint
public class InvoiceListerEndpoint {

    private static final String NAMESPACE_URI = "http://www.frkn.com/invoicedata/ws";

    @Autowired
    InvoiceRepositoryAdapter invoiceRepositoryAdapter;
    @Autowired
    private CustomerRepository customerRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getDataRequest")
    @ResponsePayload
    public GetDataResponse getData(@RequestPayload GetDataRequest request) {
        GetDataResponse response = new GetDataResponse();

        Boolean isRequestValid = request.getValidationModel().validate();

        if(isRequestValid){
            //0 -> customer data
            //1 -> invoice data
            if(request.getListType().equals(0)) {
                response.setCustomerData(customerRepository.findAll());
            }
            else if(request.getListType().equals(1)){
                response.setInvoiceData(invoiceRepositoryAdapter.getAllInvoices());
            }

            response.setMessage("Successful: List returned.");
        }
        else{
            response.setMessage("Failed: List type is invalid. Try with list type of zero(0) for customer list or one(1) for invoice list.");
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addCustomerRequest")
    @ResponsePayload
    public AddCustomerResponse addCustomer(@RequestPayload AddCustomerRequest request) {

        Boolean isRequestValid = request.getValidationModel().validate();
        AddCustomerResponse response = new AddCustomerResponse();

        if(isRequestValid){
            String customerName = request.getName();
            Customer newCustomer = new Customer(customerName);

            Customer savedCustomer = customerRepository.save(newCustomer);
            response.setId(savedCustomer.getId());
            response.setName(savedCustomer.getName());
            response.setMessage("Successful: New customer added to the system.");
        }
        else{
            response.setMessage("Failed: Customer info is invalid. Try with valid customer properties.");
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addInvoiceRequest")
    @ResponsePayload
    public AddInvoiceResponse addInvoice(@RequestPayload AddInvoiceRequest request) {
        AddInvoiceResponse response = new AddInvoiceResponse();

        Boolean isRequestValid = request.getValidationModel().validate();
        Boolean isCustomerExists = request.getValidationModel().validateBy(customerRepository);

        if(isRequestValid){
            if(isCustomerExists) {
                XMLGregorianCalendar invoicedate = request.getInvoicedate();
                LocalDateTime convertedInvoiceDateAsLocalDateTime = invoicedate.toGregorianCalendar().toZonedDateTime()
                        .toLocalDateTime();
                Double price = request.getPrice();
                Long customerId = request.getCustomerId();

                Invoice newInvoice = new Invoice(convertedInvoiceDateAsLocalDateTime, price, customerId);
                Invoice savedInvoice = invoiceRepositoryAdapter.getInvoiceRepository().save(newInvoice);

                //set invoice date as XML Gregorian Calendar for the XML response
                savedInvoice.setInvoicedate();

                response.setId(savedInvoice.getId());
                response.setInvoicedate(savedInvoice.getInvoicedate());
                response.setPrice(savedInvoice.getPrice());
                response.setCustomerId(savedInvoice.getCustomerId());
                response.setMessage("Successful: New Invoice added to the system.");
            }
            else{
                response.setMessage("Failed: Customer does not exist in the system.");
            }
        }
        else{
            response.setMessage("Failed: Invoice info is invalid. Try with valid invoice properties.");
        }

        return response;
    }
}
package com.frkn.invoicelister.repository.invoice;

import com.frkn.invoicelister.repository.customer.CustomerRepository;
import com.frkn.invoicelister.ws.Customer;
import com.frkn.invoicelister.ws.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class InvoiceRepositoryAdapter {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    public List<Invoice> getAllInvoices(){
        List<Customer> allCustomers = customerRepository.findAll();
        Map<Long, Customer> customersAsHashMap = allCustomers.stream().collect(Collectors.toMap(Customer::getId, Function.identity()));
        List<Invoice> allInvoices = invoiceRepository.findAll();

        allInvoices.forEach( invoice -> {
            invoice.setCustomer( customersAsHashMap.get(invoice.getCustomerId()));
        });

        return allInvoices;
    }

    public InvoiceRepository getInvoiceRepository() {
        return invoiceRepository;
    }
}

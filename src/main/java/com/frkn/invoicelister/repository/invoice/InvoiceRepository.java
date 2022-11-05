package com.frkn.invoicelister.repository.invoice;

import com.frkn.invoicelister.ws.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
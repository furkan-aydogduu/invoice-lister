# invoice-lister

# Project: Invoice Listing Service

### About:
- The service provides an API to manage the following basic invoicing operations:
    - Adding new customer to the system
    - Adding new invoice to the system
    - Listing customers of the system
    - Listing invoices that are associated with the customers of the system  
- The service is architectured on an XML SOAP Request Endpoint.
- The service uses a WSS-Outgoing Seucrity Authentication mechanism for the authenticated users. Any other user does not have access to the service functionalities. Authenticated system users are predefined on the database. The SOAP configuration for the WSS-Security is provided at the [WSS-Security Configuration](#wss-security-configuration) section.
- The service uses HTTPS protocol for ongoing requests and responses and runs on the port 443. Unsecure HTTP requests are redirected to HTTPS port. 
- The service only accepts "text/xml" requests and does return only "text/xml" responses.
- Every request data in the SOAP messages goes through some validation processes(like customer name, invoice date or price validations e.g.) before the endpoint returns a response. If validation fails, endpoint returns a convenient response message related to the incoming request.
- The service can be tested from a SOAP UI software. Examples of the usage of the endpoint are given at the [Test Examples of SOAP UI Client](#test-examples-of-soap-ui-client) section.
- The database architecture of the backend system are structured on two databases which are that one holds the customers and system users and the other holds the invoice data. ER diagram of the system is provided at the [ER Diagram](#er-diagram) section. 
- Related WSDL and XSD informations are provided at the [WSDL and XSD Schemas](#wsdl-and-xsd-schemas) section.
- Since the endpoints runs on the HTTPS protocol, the related JKS keystore is provided at the src/main/resources folder of the source code. The details about the keystore configuration are provided at the src/main/resources/application.properties of the source code.
- Some predefined ER data are provided in the database system. So, when the end point starts up, the listing operations are available even if any adding operation are not done on the system.  
## Technologies used:
	  - Java 8 
	  - Maven 3.8.6 
	  - H2 Database
	  - Spring Boot 2.7.0
	  - Spring Data JPA
  
  ## API Details:
  ##### Service API URL : https://{host}/ws
  ##### Service API WSDL URL : https://{host}/ws/invoiceData.wsdl
  
    {host}    : Required for all requests

## Test Examples of SOAP UI Client
  #### [addCustomer] : Method to add a new customer entry to the system.  
      Example request:
                       URL : https://localhost/ws
                       Request Body  : 
                                       <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://www.frkn.com/invoicedata/ws">
                                         <soapenv:Header/>
                                         <soapenv:Body>
                                            <ws:addCustomerRequest>
                                               <ws:name>New Customer</ws:name>
                                            </ws:addCustomerRequest>
                                         </soapenv:Body>
                                      </soapenv:Envelope>
                       Response Body : 
                                    <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
                                       <SOAP-ENV:Header/>
                                       <SOAP-ENV:Body>
                                          <ns2:addCustomerResponse xmlns:ns2="http://www.frkn.com/invoicedata/ws">
                                             <ns2:id>6</ns2:id>
                                             <ns2:name>New Customer</ns2:name>
                                             <ns2:message>Successful: New customer added to the system.</ns2:message>
                                          </ns2:addCustomerResponse>
                                       </SOAP-ENV:Body>
                                    </SOAP-ENV:Envelope>
                                
  #### [addInvoice] : Method to add a new invoice entry to the system.
      Example request:
                       URL : https://localhost/ws
                       Request Body: 
                                      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://www.frkn.com/invoicedata/ws">
                                         <soapenv:Header/>
                                         <soapenv:Body>
                                            <ws:addInvoiceRequest>
                                               <ws:invoicedate>2022-10-12</ws:invoicedate>
                                               <ws:price>999.778</ws:price>
                                               <ws:customerId>3</ws:customerId>
                                            </ws:addInvoiceRequest>
                                         </soapenv:Body>
                                      </soapenv:Envelope>
                       Response Body : 
                                    <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
                                       <SOAP-ENV:Header/>
                                       <SOAP-ENV:Body>
                                          <ns2:addInvoiceResponse xmlns:ns2="http://www.frkn.com/invoicedata/ws">
                                             <ns2:id>10</ns2:id>
                                             <ns2:invoicedate>2022-10-12T00:00:00</ns2:invoicedate>
                                             <ns2:price>999.778</ns2:price>
                                             <ns2:customerId>3</ns2:customerId>
                                             <ns2:message>Successful: New Invoice added to the system.</ns2:message>
                                          </ns2:addInvoiceResponse>
                                       </SOAP-ENV:Body>
                                    </SOAP-ENV:Envelope>
 
 #### [getData] : Method to get the list of the customers or invoices. A list type should be provided as the input parameter in the request envelope. Accepted list types are zero(0) for customer list and one(1) for the invoice list.
      Example request:
                       URL : https://localhost/ws
                       Request Body  : 
                                    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://www.frkn.com/invoicedata/ws">
                                       <soapenv:Header/>
                                       <soapenv:Body>
                                          <ws:getDataRequest>
                                             <ws:listType>0</ws:listType>  -- 0 -> customer list
                                          </ws:getDataRequest>
                                       </soapenv:Body>
                                    </soapenv:Envelope>
                       Response Body : 
                                      <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
                                         <SOAP-ENV:Header/>
                                         <SOAP-ENV:Body>
                                            <ns2:getDataResponse xmlns:ns2="http://www.frkn.com/invoicedata/ws">
                                               <ns2:customerData>
                                                  <ns2:id>1</ns2:id>
                                                  <ns2:name>customer1</ns2:name>
                                               </ns2:customerData>
                                               <ns2:customerData>
                                                  <ns2:id>2</ns2:id>
                                                  <ns2:name>customer2</ns2:name>
                                               </ns2:customerData>
                                               <ns2:customerData>
                                                  <ns2:id>3</ns2:id>
                                                  <ns2:name>customer3</ns2:name>
                                               </ns2:customerData>
                                               <ns2:customerData>
                                                  <ns2:id>4</ns2:id>
                                                  <ns2:name>customer4</ns2:name>
                                               </ns2:customerData>
                                               <ns2:message>Successful: List returned.</ns2:message>
                                            </ns2:getDataResponse>
                                         </SOAP-ENV:Body>
                                      </SOAP-ENV:Envelope>
                              
      Example request:
                       URL : https://localhost/ws
                       Request Body  : 
                                    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://www.frkn.com/invoicedata/ws">
                                       <soapenv:Header/>
                                       <soapenv:Body>
                                          <ws:getDataRequest>
                                             <ws:listType>1</ws:listType>  -- 1 ->  1 -> invoice list
                                          </ws:getDataRequest>
                                       </soapenv:Body>
                                    </soapenv:Envelope>
                       Response Body : 
                                      <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
                                         <SOAP-ENV:Header/>
                                         <SOAP-ENV:Body>
                                            <ns2:getDataResponse xmlns:ns2="http://www.frkn.com/invoicedata/ws">
                                               <ns2:invoiceData>
                                                  <ns2:id>1</ns2:id>
                                                  <ns2:invoicedate>2022-11-05T09:40:52.157</ns2:invoicedate>
                                                  <ns2:customer>
                                                     <ns2:id>1</ns2:id>
                                                     <ns2:name>customer1</ns2:name>
                                                  </ns2:customer>
                                                  <ns2:price>15.0</ns2:price>
                                               </ns2:invoiceData>
                                               <ns2:invoiceData>
                                                  <ns2:id>2</ns2:id>
                                                  <ns2:invoicedate>2022-11-05T09:40:52.158</ns2:invoicedate>
                                                  <ns2:customer>
                                                     <ns2:id>2</ns2:id>
                                                     <ns2:name>customer2</ns2:name>
                                                  </ns2:customer>
                                                  <ns2:price>35.0</ns2:price>
                                               </ns2:invoiceData>
                                               <ns2:invoiceData>
                                                  <ns2:id>3</ns2:id>
                                                  <ns2:invoicedate>2022-11-05T09:40:52.158</ns2:invoicedate>
                                                  <ns2:customer>
                                                     <ns2:id>3</ns2:id>
                                                     <ns2:name>customer3</ns2:name>
                                                  </ns2:customer>
                                                  <ns2:price>17.2</ns2:price>
                                               </ns2:invoiceData>
                                               <ns2:invoiceData>
                                                  <ns2:id>4</ns2:id>
                                                  <ns2:invoicedate>2022-11-05T09:40:52.158</ns2:invoicedate>
                                                  <ns2:customer>
                                                     <ns2:id>4</ns2:id>
                                                     <ns2:name>customer4</ns2:name>
                                                  </ns2:customer>
                                                  <ns2:price>23.42</ns2:price>
                                               </ns2:invoiceData>
                                               <ns2:message>Successful: List returned.</ns2:message>
                                            </ns2:getDataResponse>
                                         </SOAP-ENV:Body>
                                      </SOAP-ENV:Envelope>
## WSDL and XSD Schemas(#wsdl-and-xsd-schemas)

 ![alt text](https://github.com/furkan-aydogduu/invoice-lister/blob/master/src/main/resources/docs/invoice_lister_wsdl.jpg?raw=true)
 
 ![alt text](https://github.com/furkan-aydogduu/invoice-lister/blob/master/src/main/resources/docs/invoice_lister_xsd_schema.jpg?raw=true)
     
                                    
 

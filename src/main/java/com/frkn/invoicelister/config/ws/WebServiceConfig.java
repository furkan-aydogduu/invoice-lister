package com.frkn.invoicelister.config.ws;

import com.frkn.invoicelister.exception.DetailSoapFaultDefinitionExceptionResolver;
import com.frkn.invoicelister.exception.ServiceFaultException;
import com.frkn.invoicelister.model.SystemUser;
import com.frkn.invoicelister.repository.customer.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Autowired
    ApplicationContext context;

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean(name = "invoiceData")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema dataSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("InvoicesDataPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://www.frkn.com/invoicedata/ws");
        wsdl11Definition.setSchema(dataSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema dataSchema() {
        return new SimpleXsdSchema(new ClassPathResource("invoiceData.xsd"));
    }

    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver() {
        SoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();

        SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        exceptionResolver.setDefaultFault(faultDefinition);

        Properties errorMappings = new Properties();
        errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(ServiceFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());
        exceptionResolver.setExceptionMappings(errorMappings);
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }

    @Bean
    XwsSecurityInterceptor securityInterceptor() {
        CustomPasswordValidationCallbackHandler callbackHandler =
                (CustomPasswordValidationCallbackHandler) context.getBean("callbackHandler");
        XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
        securityInterceptor.setCallbackHandler(callbackHandler);
        securityInterceptor.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));
        return securityInterceptor;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        XwsSecurityInterceptor securityInterceptor = (XwsSecurityInterceptor) context.getBean("securityInterceptor");
        interceptors.add(securityInterceptor);
    }

    @Bean
    CustomPasswordValidationCallbackHandler callbackHandler() {
        CustomPasswordValidationCallbackHandler callbackHandler = new CustomPasswordValidationCallbackHandler();
        callbackHandler.setUsersMap(new HashMap<>());
        return callbackHandler;
    }

}
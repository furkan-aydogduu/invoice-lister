package com.frkn.invoicelister.config;
;
import com.frkn.invoicelister.ws.Invoice;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.frkn.invoicelister.repository.invoice",
        entityManagerFactoryRef = "invoiceEntityManagerFactory",
        transactionManagerRef = "invoiceTransactionManager",
        repositoryImplementationPostfix = "Impl"
)
public class InvoiceDataSourceJpaConfiguration {

    @Bean
    @ConfigurationProperties("ds2.datasource")
    public DataSourceProperties secondDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource secondDataSource( @Qualifier("secondDataSourceProperties") DataSourceProperties secondDataSourceProperties) {
        return secondDataSourceProperties
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean invoiceEntityManagerFactory(
            @Qualifier("secondDataSource") DataSource secondDataSource, JpaProperties jpaProperties) {
        EntityManagerFactoryBuilder entityManagerFactoryBuilder = createEntityManagerFactoryBuilder(jpaProperties);
        return entityManagerFactoryBuilder
                .dataSource(secondDataSource)
                .packages(Invoice.class)
                .persistenceUnit("invoiceUnit")
                .build();
    }

    @Bean
    public PlatformTransactionManager invoiceTransactionManager(
            @Qualifier("invoiceEntityManagerFactory") LocalContainerEntityManagerFactoryBean invoiceEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(invoiceEntityManagerFactory.getObject()));
    }

    public EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
        JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(jpaProperties);
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(), null);
    }

    private JpaVendorAdapter createJpaVendorAdapter(JpaProperties jpaProperties) {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public DataSourceInitializer invoiceDataSourceInitializer(@Qualifier("secondDataSource") DataSource secondDataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("schema_invoiceDb.sql"));
        resourceDatabasePopulator.addScript(new ClassPathResource("data_invoiceDb.sql"));

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(secondDataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }
}

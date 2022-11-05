package com.frkn.invoicelister.config;

import com.frkn.invoicelister.model.SystemUser;
import com.frkn.invoicelister.ws.Customer;
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
        basePackages = "com.frkn.invoicelister.repository.customer",
        entityManagerFactoryRef = "customerEntityManagerFactory",
        transactionManagerRef = "customerTransactionManager"
)
public class CustomerDataSourceJpaConfiguration {

    @Bean
    @ConfigurationProperties("spring.jpa")
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties firstDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource firstDataSource(@Qualifier("firstDataSourceProperties") DataSourceProperties firstDataSourceProperties) {
        return firstDataSourceProperties
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(
            @Qualifier("firstDataSource") DataSource firstDataSource, JpaProperties jpaProperties) {
            EntityManagerFactoryBuilder entityManagerFactoryBuilder = createEntityManagerFactoryBuilder(jpaProperties);
        return entityManagerFactoryBuilder
                .dataSource(firstDataSource)
                .packages(Customer.class, SystemUser.class)
                .persistenceUnit("customerUnit")
                .build();
    }

    @Bean
    public PlatformTransactionManager customerTransactionManager(
            @Qualifier("customerEntityManagerFactory") LocalContainerEntityManagerFactoryBean customerEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(customerEntityManagerFactory.getObject()));
    }

    public EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
        JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(jpaProperties);
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(), null);
    }

    private JpaVendorAdapter createJpaVendorAdapter(JpaProperties jpaProperties) {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public DataSourceInitializer customerDataSourceInitializer(@Qualifier("firstDataSource") DataSource firstDataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("schema_customerDb.sql"));
        resourceDatabasePopulator.addScript(new ClassPathResource("data_customerDb.sql"));

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(firstDataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }


}

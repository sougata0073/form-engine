package com.sougata.form_data_service.configuration;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.DriverConfigLoaderBuilderConfigurer;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DataCenterReplication;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableCassandraRepositories
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Value("${app.cassandra.contact-points}")
    private String contactPoints;

    @Value("${app.cassandra.local-data-center}")
    private String localDataCenter;

    @Value("${app.cassandra.request-timeout-seconds}")
    private Long requestTimeOutSeconds;

    @Override
    protected String getKeyspaceName() {
        return "form_data";
    }

    @Override
    protected @Nullable String getLocalDataCenter() {
        return localDataCenter;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"com.sougata.form_data_service.model"};
    }

    @Override
    protected @Nullable DriverConfigLoaderBuilderConfigurer getDriverConfigLoaderBuilderConfigurer() {
        return builder -> builder.withDuration(
                DefaultDriverOption.REQUEST_TIMEOUT,
                Duration.ofSeconds(requestTimeOutSeconds)
        );
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {

        var formData = CreateKeyspaceSpecification.createKeyspace("form_data")
                .ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withNetworkReplication(
                        DataCenterReplication.of("north", 1),
                        DataCenterReplication.of("east", 1),
                        DataCenterReplication.of("west", 1),
                        DataCenterReplication.of("south", 1)
                );

        return List.of(formData);
    }
}

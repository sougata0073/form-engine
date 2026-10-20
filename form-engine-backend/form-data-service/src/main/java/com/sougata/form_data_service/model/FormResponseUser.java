package com.sougata.form_data_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("form_responses_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormResponseUser {

    @PrimaryKey
    private PartitionKey key;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @PrimaryKeyClass
    public static class PartitionKey {

        @PrimaryKeyColumn(name = "form_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private UUID formId;

        @PrimaryKeyColumn(name = "user_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private UUID userId;

    }

}

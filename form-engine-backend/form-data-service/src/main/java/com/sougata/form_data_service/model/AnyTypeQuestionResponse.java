package com.sougata.form_data_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AnyTypeQuestionResponse {

    @PrimaryKey
    private PartitionKey key;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @PrimaryKeyClass
    public static class PartitionKey {

        @PrimaryKeyColumn(name = "question_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long questionId;

        @PrimaryKeyColumn(name = "question_response_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long questionResponseId;

    }

}

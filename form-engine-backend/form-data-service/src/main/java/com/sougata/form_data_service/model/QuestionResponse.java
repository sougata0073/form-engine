package com.sougata.form_data_service.model;

import com.github.f4b6a3.tsid.TsidCreator;
import com.sougata.form_engine.constant.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

@Table("question_responses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QuestionResponse {

    @PrimaryKey
    private PartitionKey key;

    @Column("question_type")
    @CassandraType(type = CassandraType.Name.TEXT)
    private QuestionType questionType;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @PrimaryKeyClass
    public static class PartitionKey {

        @PrimaryKeyColumn(name = "question_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long questionId;

        @PrimaryKeyColumn(name = "form_response_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long formResponseId;

        @PrimaryKeyColumn(name = "question_response_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
        private Long questionResponseId = TsidCreator.getTsid().toLong();

    }

}

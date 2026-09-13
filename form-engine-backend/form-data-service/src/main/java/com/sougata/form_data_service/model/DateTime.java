package com.sougata.form_data_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;

@Table("date_times")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DateTime extends AnyTypeQuestionResponse {

    @Column("date_time")
    private Instant dateTime;
}

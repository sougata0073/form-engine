package com.sougata.form_data_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;

@Table("dates")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Date extends AnyTypeQuestionResponse {

    @Column("date")
    private Instant date;

}

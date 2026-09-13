package com.sougata.form_data_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("durations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Duration extends AnyTypeQuestionResponse {

    @Column("hours")
    private Integer hours;

    @Column("minutes")
    private Integer minutes;

    @Column("seconds")
    private Integer seconds;
}

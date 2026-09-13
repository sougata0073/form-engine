package com.sougata.form_data_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("paragraphs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Paragraph extends AnyTypeQuestionResponse {

    @Column("text")
    private String text;

}

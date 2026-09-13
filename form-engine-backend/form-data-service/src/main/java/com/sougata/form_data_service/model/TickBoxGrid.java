package com.sougata.form_data_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Frozen;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Map;
import java.util.Set;

@Table("tick_box_grids")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TickBoxGrid extends AnyTypeQuestionResponse {

    @Column("responses")
    private Map<Long, @Frozen Set<Long>> responses;

}

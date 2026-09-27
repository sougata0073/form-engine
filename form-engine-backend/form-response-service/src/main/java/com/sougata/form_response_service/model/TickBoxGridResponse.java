package com.sougata.form_response_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(
        name = "tick_box_grid_responses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "tick_box_grid_uk_row_id_question_id",
                        columnNames = {
                                "row_id",
                                "question_id"
                        }
                )
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
@DynamicUpdate
public class TickBoxGridResponse extends AnyTypeQuestionResponse {

        @Column(nullable = false)
        private Long rowId;


}

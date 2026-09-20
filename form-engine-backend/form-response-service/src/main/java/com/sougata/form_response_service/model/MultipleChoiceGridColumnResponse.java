package com.sougata.form_response_service.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "multiple_choice_grid_column_responses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldNameConstants
@DynamicUpdate
public class MultipleChoiceGridColumnResponse {

    @Id
    @Tsid
    private Long id;

    @Column(nullable = false)
    private Long rowId;

    @Column(nullable = false)
    private Long columnId;

    @Column(nullable = false)
    private Long responseCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MultipleChoiceGridResponse multipleChoiceGridResponse;

}

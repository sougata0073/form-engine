package com.sougata.form_response_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AnyTypeQuestionResponse {

    @Id
    private Long questionResponseId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, unique = true, name = "question_response_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private QuestionResponse questionResponse;

}

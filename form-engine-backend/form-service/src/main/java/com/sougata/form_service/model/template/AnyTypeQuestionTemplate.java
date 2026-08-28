package com.sougata.form_service.model.template;

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
public class AnyTypeQuestionTemplate {

    @Id
    private Long questionTemplateId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private QuestionTemplate questionTemplate;

}

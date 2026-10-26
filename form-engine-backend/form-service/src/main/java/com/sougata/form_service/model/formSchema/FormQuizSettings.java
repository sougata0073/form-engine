package com.sougata.form_service.model.formSchema;

import com.sougata.form_engine.constant.FormQuizReleaseMarksOption;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@Table(name = "form_quiz_settings", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class FormQuizSettings {

    @Id
    private UUID formId;

    @Column(nullable = false)
    private Boolean isQuiz = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormQuizReleaseMarksOption releaseMarksOption = FormQuizReleaseMarksOption.IMMEDIATELY;

    @Column(nullable = false)
    private Boolean respondersCanSeeIncorrectAnswers = true;

    @Column(nullable = false)
    private Boolean respondersCanSeeCorrectAnswers = true;

    @Column(nullable = false)
    private Boolean respondersCanSeePointsReceived = true;

    @Column(nullable = false)
    private Integer questionPointValue = 0;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Form form;
}

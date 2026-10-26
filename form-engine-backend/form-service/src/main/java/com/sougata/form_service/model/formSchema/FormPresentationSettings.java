package com.sougata.form_service.model.formSchema;

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
@Table(name = "form_presentation_settings", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class FormPresentationSettings {

    @Id
    private UUID formId;

    @Column(nullable = false)
    private Boolean showProgressBar = false;

    @Column(nullable = false)
    private Boolean shuffleQuestionOrder = false;

    @Column(columnDefinition = "text")
    private String responseSavedConfirmationMessage = "Your response has been recorded";

    @Column(nullable = false)
    private Boolean showLinkToSubmitAnotherResponse = false;

    @Column(nullable = false)
    private Boolean shareResponseSummaryWithResponders = false;

    @Column(nullable = false)
    private Boolean disableAutoSaveForAllRespondents = false;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Form form;

}

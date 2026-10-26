package com.sougata.form_service.model.formSchema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "form_general_settings", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class FormGeneralSettings {

    @Id
    private UUID formId;

    @Column(nullable = false)
    private Boolean published = false;

    @Column(nullable = false)
    private Boolean acceptingResponse = false;

    @Column(columnDefinition = "text")
    private String notAcceptingResponseMessage = "This form is not accepting responses.";

    private Instant stopAcceptingResponseOn;

    private Integer responseLimit;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Form form;

}

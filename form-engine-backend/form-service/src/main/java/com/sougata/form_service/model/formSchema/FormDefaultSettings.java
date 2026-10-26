package com.sougata.form_service.model.formSchema;

import com.sougata.form_engine.constant.FormEmailCollectionOption;
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
@Table(name = "form_default_settings", schema = "form_schema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class FormDefaultSettings {

    @Id
    private UUID formId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormEmailCollectionOption defaultEmailCollectionOption = FormEmailCollectionOption.RESPONDER_INPUT;

    @Column(nullable = false)
    private Boolean makeQuestionsRequiredByDefault = false;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Form form;

}

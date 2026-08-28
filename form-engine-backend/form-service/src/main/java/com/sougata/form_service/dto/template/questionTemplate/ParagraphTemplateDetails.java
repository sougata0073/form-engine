package com.sougata.form_service.dto.template.questionTemplate;

import com.sougata.form_service.validation.configuration.ValidationConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphTemplateDetails extends QuestionTemplateDetails {
    private ValidationConfig validationConfig;
}

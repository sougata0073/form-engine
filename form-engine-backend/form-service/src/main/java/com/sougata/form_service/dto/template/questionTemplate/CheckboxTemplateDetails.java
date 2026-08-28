package com.sougata.form_service.dto.template.questionTemplate;

import com.sougata.form_service.validation.configuration.ValidationConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckboxTemplateDetails extends QuestionTemplateDetails {

    private List<CheckboxOptionTemplateDetails> options;
    private ValidationConfig validationConfig;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckboxOptionTemplateDetails {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String option;
        private Integer orderIndex;
    }

}

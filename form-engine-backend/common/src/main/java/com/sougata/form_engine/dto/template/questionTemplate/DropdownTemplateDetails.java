package com.sougata.form_engine.dto.template.questionTemplate;

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
public class DropdownTemplateDetails extends QuestionTemplateDetails {

    private List<DropdownOptionTemplateDetails> options;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DropdownOptionTemplateDetails {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String option;
        private Integer orderIndex;
    }

}

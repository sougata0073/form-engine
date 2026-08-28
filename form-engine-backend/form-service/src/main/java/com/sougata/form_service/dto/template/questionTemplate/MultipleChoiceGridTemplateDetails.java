package com.sougata.form_service.dto.template.questionTemplate;

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
public class MultipleChoiceGridTemplateDetails extends QuestionTemplateDetails {
    private Boolean eachRowRequired;
    private List<MultipleChoiceGridRowTemplateDetails> rows;
    private List<MultipleChoiceGridColumnTemplateDetails> columns;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MultipleChoiceGridRowTemplateDetails {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String row;
        private Integer orderIndex;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MultipleChoiceGridColumnTemplateDetails {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String column;
        private Integer orderIndex;
    }
}

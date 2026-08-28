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
public class TickBoxGridTemplateDetails extends QuestionTemplateDetails {
    private Boolean eachRowRequired;
    private List<TickBoxGridRowTemplateDetails> rows;
    private List<TickBoxGridColumnTemplateDetails> columns;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TickBoxGridRowTemplateDetails {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String row;
        private Integer orderIndex;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TickBoxGridColumnTemplateDetails {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String column;
        private Integer orderIndex;
    }
}

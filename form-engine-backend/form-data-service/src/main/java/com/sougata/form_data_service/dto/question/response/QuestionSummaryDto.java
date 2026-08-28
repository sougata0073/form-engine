package com.sougata.form_data_service.dto.question.response;


import com.sougata.form_data_service.constant.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSummaryDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String question;
    private QuestionType questionType;
    private Integer orderIndex;
}

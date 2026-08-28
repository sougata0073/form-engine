package com.sougata.form_engine.dto.template.questionTemplate;

import com.sougata.form_engine.constant.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTemplateSummary {
    private Long id;
    private QuestionType questionType;
}

package com.sougata.form_service.dto.template;

import com.sougata.form_service.dto.template.questionTemplate.QuestionTemplateDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDetails {
    private Long id;
    private String name;
    private String title;
    private String description;
    private TemplateCategoryDetails category;
    private List<QuestionTemplateDetails> questionTemplates;
}

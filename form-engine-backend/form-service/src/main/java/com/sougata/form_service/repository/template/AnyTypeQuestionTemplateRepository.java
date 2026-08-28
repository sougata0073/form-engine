package com.sougata.form_service.repository.template;

import com.sougata.form_service.model.template.AnyTypeQuestionTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AnyTypeQuestionTemplateRepository<QT extends AnyTypeQuestionTemplate, ID> extends JpaRepository<QT, ID> {

}

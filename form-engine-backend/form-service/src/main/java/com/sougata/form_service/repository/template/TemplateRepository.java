package com.sougata.form_service.repository.template;

import com.sougata.form_service.constant.cacheNames.TemplateCacheNames;
import com.sougata.form_service.dto.template.TemplateSummaryDto;
import com.sougata.form_service.model.template.Template;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    @Cacheable(cacheNames = {TemplateCacheNames.TEMPLATE_SUMMARIES})
    @Query("""
            select
            new com.sougata.form_service.dto.template.TemplateSummaryDto(
                t.id,
                t.name,
                t.category.name
            )
            from Template t
            """)
    List<TemplateSummaryDto> getAllTemplateSummaries();

}

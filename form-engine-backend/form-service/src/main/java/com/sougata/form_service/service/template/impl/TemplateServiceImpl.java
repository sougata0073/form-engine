package com.sougata.form_service.service.template.impl;

import com.sougata.form_engine.dto.template.TemplateSummariesDto;
import com.sougata.form_engine.dto.template.TemplateSummaryDto;
import com.sougata.form_engine.dto.template.TemplateToFormBuildResDto;
import com.sougata.form_service.exception.TemplateNotFoundException;
import com.sougata.form_service.model.template.RecentlyUsedTemplate;
import com.sougata.form_service.repository.template.RecentlyUsedTemplateRepository;
import com.sougata.form_service.repository.template.TemplateRepository;
import com.sougata.form_service.service.formSchema.FormService;
import com.sougata.form_service.service.template.TemplateService;
import com.sougata.form_service.service.template.TemplateServiceCached;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final RecentlyUsedTemplateRepository recentlyUsedTemplateRepository;
    private final TemplateServiceCached templateServiceCached;
    private final FormService formService;

    @Autowired
    public TemplateServiceImpl(TemplateRepository templateRepository, RecentlyUsedTemplateRepository recentlyUsedTemplateRepository, TemplateServiceCached templateServiceCached, FormService formService) {
        this.templateRepository = templateRepository;
        this.recentlyUsedTemplateRepository = recentlyUsedTemplateRepository;
        this.templateServiceCached = templateServiceCached;
        this.formService = formService;
    }

    @Override
    public TemplateSummariesDto getAllTemplateSummaries(UUID userId) {
        var recentTemplates = templateServiceCached.getRecentlyUsedTemplates(userId);
        var allTemplates = templateRepository.getAllTemplateSummaries();

        var merged = new ArrayList<TemplateSummaryDto>();

        merged.addAll(recentTemplates);
        merged.addAll(allTemplates);

        return new TemplateSummariesDto(merged);
    }

    @Override
    @Transactional
    public TemplateToFormBuildResDto buildFormFromTemplate(Long templateId, UUID userId) {
        var savedForm = formService.createFromTemplate(
                templateServiceCached.getTemplateDetails(templateId), userId
        );

        RecentlyUsedTemplate rt = new RecentlyUsedTemplate();

        rt.setUserId(userId);
        rt.setTemplate(
                templateRepository.findById(templateId)
                        .orElseThrow(() -> new TemplateNotFoundException(templateId))
        );

        if (recentlyUsedTemplateRepository.existsByUserIdAndTemplateId(userId, templateId)) {
            recentlyUsedTemplateRepository.deleteByUserAndTemplate(userId, templateId);
        } else {
            recentlyUsedTemplateRepository.deleteOld(userId, 5);
        }

        recentlyUsedTemplateRepository.save(rt);

        return new TemplateToFormBuildResDto(savedForm.getId());
    }

}

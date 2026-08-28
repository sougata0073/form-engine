package com.sougata.form_service.service.template.impl;

import com.sougata.form_service.constant.cacheNames.TemplateCacheNames;
import com.sougata.form_service.dto.template.TemplateCategoryDetails;
import com.sougata.form_service.dto.template.TemplateDetails;
import com.sougata.form_service.dto.template.TemplateSummaryDto;
import com.sougata.form_service.dto.template.questionTemplate.QuestionTemplateDetails;
import com.sougata.form_service.dto.template.questionTemplate.QuestionTemplateSummary;
import com.sougata.form_service.exception.TemplateNotFoundException;
import com.sougata.form_service.repository.template.AnyQuestionTemplateRepositoryFactory;
import com.sougata.form_service.repository.template.QuestionTemplateRepository;
import com.sougata.form_service.repository.template.RecentlyUsedTemplateRepository;
import com.sougata.form_service.repository.template.TemplateRepository;
import com.sougata.form_service.service.template.TemplateServiceCached;
import com.sougata.form_service.service.template.questionTemplateManager.QuestionTemplateManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateServiceCachedImpl implements TemplateServiceCached {

    private final RecentlyUsedTemplateRepository recentlyUsedTemplateRepository;
    private final TemplateRepository templateRepository;
    private final QuestionTemplateRepository questionTemplateRepository;
    private final AnyQuestionTemplateRepositoryFactory anyTypeQuestionTemplateRepositoryFactory;
    private final QuestionTemplateManagerFactory questionTemplateManagerFactory;

    @Autowired
    @Lazy
    private TemplateServiceCached self;

    @Cacheable(cacheNames = {TemplateCacheNames.RECENTLY_USED_TEMPLATES}, key = "#userId")
    @Override
    public List<TemplateSummaryDto> getRecentlyUsedTemplates(UUID userId) {
        return recentlyUsedTemplateRepository.getByUserId(userId);
    }

    @Cacheable(cacheNames = {TemplateCacheNames.TEMPLATE_DETAILS}, key = "#templateId")
    @Override
    public TemplateDetails getTemplateDetails(Long templateId) {
        return self.loadTemplateDetailsFromDb(templateId);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public TemplateDetails loadTemplateDetailsFromDb(Long templateId) {
        var template = templateRepository.findById(templateId)
                .orElseThrow(() -> new TemplateNotFoundException(templateId));

        var templateDetails = new TemplateDetails();

        templateDetails.setId(template.getId());
        templateDetails.setTitle(template.getTitle());
        templateDetails.setName(template.getName());
        templateDetails.setDescription(template.getDescription());

        var category = template.getCategory();
        templateDetails.setCategory(new TemplateCategoryDetails(category.getId(), category.getName()));

        List<QuestionTemplateDetails> questionTemplates = new ArrayList<>();
        var questions = questionTemplateRepository.findQuestionTemplateSummariesByTemplateId(template.getId());
        var questionTypeMap = questions.stream().collect(Collectors.groupingBy(QuestionTemplateSummary::getQuestionType));

        questionTypeMap.keySet().forEach(qType -> {
            var repo = anyTypeQuestionTemplateRepositoryFactory.get(qType);
            var manager = questionTemplateManagerFactory.get(qType);
            var qIds = questionTypeMap.get(qType).stream().map(QuestionTemplateSummary::getId).collect(Collectors.toList());

            var qs = repo.findAllById((Iterable<Object>) (Iterable<?>) qIds).stream().map(manager::toQuestionTemplateDetails).toList();

            questionTemplates.addAll(qs);
        });

        questionTemplates.sort(Comparator.comparingInt(QuestionTemplateDetails::getOrderIndex));

        templateDetails.setQuestionTemplates(questionTemplates);

        return templateDetails;
    }
}

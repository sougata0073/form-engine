package com.sougata.form_response_service.feignClient;

import com.sougata.form_engine.dto.form.FormDetailsDto;
import com.sougata.form_engine.dto.form.FormInfoDto;
import com.sougata.form_engine.dto.question.details.QuestionDetailsDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummariesDto;
import com.sougata.form_engine.dto.question.summary.QuestionSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient("form-service")
public interface FormServiceFeignClient {

    @GetMapping(path = "api/v1/forms/{formId}/question-summaries")
    QuestionSummariesDto getQuestionSummaries(@PathVariable("formId") UUID formId);

    @GetMapping(path = "api/v1/forms/{formId}/details")
    FormDetailsDto getFormDetails(@PathVariable("formId") UUID formId);

    @GetMapping(path = "api/v1/forms/{formId}/questions/{questionId}/summary")
    QuestionSummaryDto getQuestionSummary(@PathVariable("formId") UUID formId, @PathVariable("questionId") Long questionId);

    @GetMapping(path = "api/v1/forms/{formId}/questions/{questionId}")
    QuestionDetailsDto getQuestion(@PathVariable("formId") UUID formId, @PathVariable("questionId") Long questionId);

    @GetMapping(path = "api/v1/forms/{formId}/info")
    FormInfoDto getFormInfo(@PathVariable("formId") UUID formId);
}

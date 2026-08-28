package com.sougata.form_engine.dto.formResponse.question;

import com.sougata.form_engine.dto.question.details.CheckboxDetailsDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CheckboxResponseQuestionDto extends ResponseQuestionDto<CheckboxResponseQuestionDto.Response> {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response extends ResponseByQuestionResponse {
        private List<String> optionIds;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Summary extends ResponseByQuestionSummary {
        private List<CheckboxDetailsDto.Option> options;
    }

}

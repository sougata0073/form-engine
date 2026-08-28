package com.sougata.form_service.dto.form;

import com.sougata.form_service.dto.question.response.QuestionDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormDetailsDto {
    private UUID id;
    private String name;
    private String title;
    private String description;
    private Boolean published;
    private Boolean acceptingResponse;
    private String notAcceptingResponseMessage;
    private Instant stopAcceptingResponseOn;
    private Integer stopAcceptingResponseAfterResponse;
    private Instant lastOpenedOn;
    private List<QuestionDetails> questions;
}

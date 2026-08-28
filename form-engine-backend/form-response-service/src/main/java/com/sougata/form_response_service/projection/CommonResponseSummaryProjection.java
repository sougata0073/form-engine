package com.sougata.form_response_service.projection;

public record CommonResponseSummaryProjection(
        Long questionId,
        Long numberOfResponses
) {
}

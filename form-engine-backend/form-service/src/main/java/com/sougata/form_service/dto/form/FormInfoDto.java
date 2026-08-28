package com.sougata.form_service.dto.form;

import com.sougata.form_service.model.formSchema.Form;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormInfoDto {
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

    public static FormInfoDto create(Form form) {
        return new FormInfoDto(
                form.getId(),
                form.getName(),
                form.getTitle(),
                form.getDescription(),
                form.getPublished(),
                form.getAcceptingResponse(),
                form.getNotAcceptingResponseMessage(),
                form.getStopAcceptingResponseOn(),
                form.getStopAcceptingResponseAfterResponse(),
                form.getLastOpenedOn()
        );
    }
}

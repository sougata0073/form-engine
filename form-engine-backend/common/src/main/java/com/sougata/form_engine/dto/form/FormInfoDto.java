package com.sougata.form_engine.dto.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;
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
    private Instant lastOpenedOn;
    private FormDefaultSettingsDto defaultSettings;
    private FormGeneralSettingsDto generalSettings;
    private FormPresentationSettingsDto presentationSettings;
    private FormQuizSettingsDto quizSettings;
    private FormResponseSettingsDto responseSettings;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FormInfoDto that = (FormInfoDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(title, that.title)
                && Objects.equals(description, that.description)
                && Objects.equals(lastOpenedOn, that.lastOpenedOn)
                && Objects.equals(defaultSettings, that.defaultSettings)
                && Objects.equals(generalSettings, that.generalSettings)
                && Objects.equals(presentationSettings, that.presentationSettings)
                && Objects.equals(quizSettings, that.quizSettings)
                && Objects.equals(responseSettings, that.responseSettings);
    }

}

package com.sougata.form_engine.dto.form;

import com.sougata.form_engine.constant.FormEmailCollectionOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormDefaultSettingsDto {
    private FormEmailCollectionOption defaultEmailCollectionOption;
    private Boolean makeQuestionsRequiredByDefault;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FormDefaultSettingsDto that = (FormDefaultSettingsDto) o;
        return defaultEmailCollectionOption == that.defaultEmailCollectionOption
                && Objects.equals(makeQuestionsRequiredByDefault, that.makeQuestionsRequiredByDefault);
    }
}

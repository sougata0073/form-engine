package com.sougata.form_engine.dto.form;

import com.sougata.form_engine.constant.FormEmailCollectionOption;
import com.sougata.form_engine.constant.ResponseCopySendOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormResponseSettingsDto {
    private FormEmailCollectionOption emailCollectionOption;
    private ResponseCopySendOption responseCopySendOption;
    private Boolean isResponseEditable;
    private Boolean singleResponseOnly;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FormResponseSettingsDto that = (FormResponseSettingsDto) o;
        return emailCollectionOption == that.emailCollectionOption
                && responseCopySendOption == that.responseCopySendOption
                && Objects.equals(isResponseEditable, that.isResponseEditable)
                && Objects.equals(singleResponseOnly, that.singleResponseOnly);
    }

}

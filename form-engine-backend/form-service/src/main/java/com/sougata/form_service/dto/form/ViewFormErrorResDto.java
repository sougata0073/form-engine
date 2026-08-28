package com.sougata.form_service.dto.form;

import com.sougata.form_service.constant.ViewFormErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewFormErrorResDto {
    private String formTitle;
    private ViewFormErrorReason reason;
    private String message;
}

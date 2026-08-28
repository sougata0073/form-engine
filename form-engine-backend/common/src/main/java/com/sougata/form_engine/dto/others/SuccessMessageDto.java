package com.sougata.form_engine.dto.others;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuccessMessageDto {
    private String message;

    public static SuccessMessageDto create(String message) {
        return new SuccessMessageDto(message);
    }
}
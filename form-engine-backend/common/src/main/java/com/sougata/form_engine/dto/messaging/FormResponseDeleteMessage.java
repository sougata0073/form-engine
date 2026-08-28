package com.sougata.form_engine.dto.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormResponseDeleteMessage {
    private UUID formId;
    private Long formResponseId;
    private UUID userId;
}

package com.sougata.form_data_service.constant;

import lombok.Getter;

@Getter
public enum QuestionType {
    CHECKBOX("Checkbox"),
    DATE("Date"),
    DATE_TIME("Date and Time"),
    DROPDOWN("Dropdown"),
    DURATION("Duration"),
    FILE_UPLOAD("File Upload"),
    LINEAR_SCALE("Linear Scale"),
    MULTIPLE_CHOICE("Multiple Choice"),
    MULTIPLE_CHOICE_GRID("Multiple Choice Grid"),
    PARAGRAPH("Paragraph"),
    RATING("Rating"),
    SHORT_ANSWER("Short Answer"),
    TICK_BOX_GRID("Tick Box Grid"),
    TIME("Time");

    private final String displayName;

    QuestionType(String displayName) {
        this.displayName = displayName;
    }

}

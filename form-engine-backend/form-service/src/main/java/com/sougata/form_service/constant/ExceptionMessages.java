package com.sougata.form_service.constant;

public class ExceptionMessages {

    public static final String FORM_NOT_FOUND = "Form not found with ID: %s";
    public static final String QUESTION_MANAGER_NOT_FOUND = "No question manager found for question type: %s";
    public static final String JSON_PARSING_EXCEPTION = "Error parsing JSON: %s";
    public static final String QUESTION_NOT_FOUND = "%s not found with ID: %d";
    public static final String INVALID_VALIDATION_ID = "Invalid validation ID: %s for question type: %s";
    public static final String INVALID_SELECT_AT_LEAST = "Please select at least %d option(s). Currently %d selected.";
    public static final String INVALID_SELECT_AT_MOST = "Please select at most %d option(s). Currently %d selected.";
    public static final String INVALID_SELECT_EXACTLY = "Please select exactly %d option(s). Currently %d selected.";
    public static final String RESPONSE_VALIDATOR_NOT_FOUND = "No response validator found for validation ID: %s";
    public static final String INVALID_DROPDOWN_SELECTED_INDEX = "Selected index must be between 0 and %d";
    public static final String INVALID_FILE_TYPE = "File type not allowed. Provided type: %s. Allowed type(s): %s";
    public static final String INVALID_FILE_SIZE = "Uploaded file size is %d MB but maximum permitted file size is %d MB";
    public static final String INVALID_SCALE = "Maximum scale limit is %d but provided %d";
    public static final String INVALID_MULTIPLE_CHOICE_GRID_ROW_LENGTH = "Expected %d rows but found %d";
    public static final String INVALID_TICK_BOX_GRID_ROW_LENGTH = "Expected %d rows but found %d";
    public static final String FILE_TYPE_NOT_FOUND = "File type not found with category %s";
}

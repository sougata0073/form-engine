package com.sougata.form_service.constant;

public class ExceptionMessages {

    public static final String FORM_NOT_FOUND = "Form not found with ID: %s";
    public static final String QUESTION_MANAGER_NOT_FOUND = "No question manager found for question type: %s";
    public static final String JSON_PARSING_EXCEPTION = "Error parsing JSON: %s";
    public static final String QUESTION_NOT_FOUND = "%s not found with ID: %d";
    public static final String RESPONSE_VALIDATOR_NOT_FOUND = "No response validator found for validation ID: %s";
    public static final String INVALID_FILE_TYPE = "File type not allowed. Provided type: %s. Allowed type(s): %s";
    public static final String INVALID_FILE_SIZE = "Uploaded file size is %d MB but maximum permitted file size is %d MB";
    public static final String INVALID_SCALE = "Maximum scale limit is %d but provided %d";
    public static final String FILE_TYPE_NOT_FOUND = "File type not found with category %s";
}

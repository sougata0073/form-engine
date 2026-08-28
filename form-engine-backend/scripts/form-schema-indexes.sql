CREATE INDEX idx_form_responses_form_created_at
    ON form_responses (form_id, created_at);

CREATE INDEX idx_form_responses_form_id_id
    ON form_responses (form_id, id);

CREATE INDEX idx_question_responses_form_response_question
    ON question_responses (form_response_id, question_id);

CREATE INDEX idx_checkbox_options_checkbox_response_option
    ON checkbox_options (checkbox_id, response_option_id);

CREATE INDEX idx_multiple_choice_grid_rows_grid_row_column
    ON multiple_choice_grid_rows
        (multiple_choice_grid_id, row_id, response_column_id);

CREATE INDEX idx_tick_box_grid_rows_grid_row
    ON tick_box_grid_rows (tick_box_grid_id, row_id);

CREATE INDEX idx_tick_box_grid_columns_row_option
    ON tick_box_grid_columns
        (tick_box_grid_row_id, response_option_id);
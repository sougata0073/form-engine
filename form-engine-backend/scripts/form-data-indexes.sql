create index if not exists idx_form_responses_form_id
    on form_responses (form_id);

create index if not exists idx_qr_form_response_id_question_id
    on question_responses (form_response_id, question_id);

create index if not exists idx_short_answers_qr_id
    on short_answers (question_response_id);

create index if not exists idx_short_answers_text_ops
    on short_answers (text text_pattern_ops);

create index if not exists idx_paragraphs_qr_id
    on paragraphs (question_response_id);

create index if not exists idx_paragraphs_text_ops
    on paragraphs (text text_pattern_ops);

create index if not exists idx_dropdowns_qr_id
    on dropdowns (question_response_id);

create index if not exists idx_dropdowns_response_option_id
    on dropdowns (response_option_id);

create index if not exists idx_multiple_choices_qr_id
    on multiple_choices (question_response_id);

create index if not exists idx_multiple_choices_response_option_id
    on multiple_choices (response_option_id);

create index if not exists idx_checkboxes_qr_id
    on checkboxes (question_response_id);

create index if not exists idx_checkbox_options_checkbox_id_option_id
    on checkbox_options (checkbox_id, response_option_id);

create index if not exists idx_linear_scales_qr_id_scale
    on linear_scales (question_response_id, scale);

create index if not exists idx_ratings_qr_id_rating
    on ratings (question_response_id, rating);

create index if not exists idx_dates_qr_id_date
    on dates (question_response_id, date);

create index if not exists idx_times_qr_id_time
    on times (question_response_id, time);

create index if not exists idx_date_times_qr_id_date_time
    on date_times (question_response_id, date_time);

create index if not exists idx_durations_qr_id
    on durations (question_response_id);

create index if not exists idx_file_uploads_qr_id
    on file_uploads (question_response_id);

create index if not exists idx_mc_grids_qr_id
    on multiple_choice_grids (question_response_id);

create index if not exists idx_mc_grid_rows_grid_id_row_id_col_id
    on multiple_choice_grid_rows (multiple_choice_grid_id, row_id, response_column_id);

create index if not exists idx_tick_box_grids_qr_id
    on tick_box_grids (question_response_id);

create index if not exists idx_tick_box_grid_rows_grid_id_row_id
    on tick_box_grid_rows (tick_box_grid_id, row_id);

create index if not exists idx_tick_box_grid_cols_row_id_option_id
    on tick_box_grid_columns (tick_box_grid_row_id, response_option_id);
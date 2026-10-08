create index if not exists idx_form_response_individuals_form_id
    on form_response_individuals(form_response_summary_form_id);

create index if not exists idx_question_response_summaries_form_response_summary_form_id
    on question_response_summaries(form_response_summary_form_id);

create index if not exists idx_question_responses_question_response_summary_question_id
    on question_responses(question_response_summary_question_id);

create index if not exists idx_question_response_form_response_individual_form_response_individual_id
    on question_response_form_response_individual(form_response_individual_id);

create index if not exists idx_question_response_form_response_individual_question_response_id
    on question_response_form_response_individual(question_response_id);

create index if not exists idx_checkbox_responses_question_id
    on checkbox_responses(question_id);

create index if not exists idx_date_responses_question_id
    on date_responses(question_id);

create index if not exists idx_date_time_responses_question_id
    on date_time_responses(question_id);

create index if not exists idx_dropdown_responses_question_id
    on dropdown_responses(question_id);

create index if not exists idx_duration_responses_question_id
    on duration_responses(question_id);

create index if not exists idx_file_upload_responses_question_id
    on file_upload_responses(question_id);

create index if not exists idx_linear_scale_responses_question_id
    on linear_scale_responses(question_id);

create index if not exists idx_multiple_choice_grid_responses_question_id
    on multiple_choice_grid_responses(question_id);

create index if not exists idx_multiple_choice_responses_question_id
    on multiple_choice_responses(question_id);

create index if not exists idx_paragraph_responses_question_id
    on paragraph_responses(question_id);

create index if not exists idx_rating_responses_question_id
    on rating_responses(question_id);

create index if not exists idx_short_answer_responses_question_id
    on short_answer_responses(question_id);

create index if not exists idx_tick_box_grid_responses_question_id
    on tick_box_grid_responses(question_id);

create index if not exists idx_time_responses_question_id
    on time_responses(question_id);
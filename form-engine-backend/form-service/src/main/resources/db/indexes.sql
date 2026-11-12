create index if not exists idx_questions_form_id
    on form_schema.questions(form_id);

create index if not exists idx_questions_form_id_question_type
    on form_schema.questions(form_id, question_type);

create index if not exists idx_questions_id_form_id
    on form_schema.questions(id, form_id);

create index if not exists idx_forms_user_id
    on form_schema.forms(user_id);
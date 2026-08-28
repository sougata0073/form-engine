BEGIN;

-- ============================================================
-- TEMPLATE
-- ============================================================

INSERT INTO form_template.templates (
    id,
    created_at,
    last_modified_at,
    description,
    name,
    title,
    category_id
)
VALUES (
           875826790815802867,
           '2026-08-14T18:22:26.769281Z',
           NULL,
           E'Thank you for participating in our event. We hope you had as much fun attending as we did organizing it.\n\nWe want to hear your feedback so we can keep improving our logistics and content. Please fill this quick survey and let us know your thoughts (your answers will be anonymous).',
           'Event feedback',
           'Event feedback',
           (SELECT id
            FROM form_template.template_categories
            WHERE name = 'Work')
       );


-- ============================================================
-- QUESTION 1
-- How satisfied were you with the event?
-- LINEAR_SCALE 1-5
-- ============================================================

INSERT INTO form_template.question_templates (
    id, created_at, last_modified_at, description,
    order_index, question, question_type, required, template_id
)
VALUES (
           875826790815802868,
           '2026-08-14T18:22:26.769281Z',
           NULL,
           NULL,
           0,
           'How satisfied were you with the event?',
           'LINEAR_SCALE',
           TRUE,
           875826790815802867
       );

INSERT INTO form_template.linear_scale_templates (
    from_number,
    to_number,
    question_template_id
)
VALUES (
           1,
           5,
           875826790815802868
       );


-- ============================================================
-- QUESTION 2
-- How relevant and helpful do you think it was for your job?
-- LINEAR_SCALE 1-5
-- ============================================================

INSERT INTO form_template.question_templates (
    id, created_at, last_modified_at, description,
    order_index, question, question_type, required, template_id
)
VALUES (
           875826790815802869,
           '2026-08-14T18:22:26.769281Z',
           NULL,
           NULL,
           1,
           'How relevant and helpful do you think it was for your job?',
           'LINEAR_SCALE',
           TRUE,
           875826790815802867
       );

INSERT INTO form_template.linear_scale_templates (
    from_number,
    to_number,
    question_template_id
)
VALUES (
           1,
           5,
           875826790815802869
       );


-- ============================================================
-- QUESTION 3
-- What were your key take aways from this event?
-- SHORT_ANSWER
-- ============================================================

INSERT INTO form_template.question_templates (
    id, created_at, last_modified_at, description,
    order_index, question, question_type, required, template_id
)
VALUES (
           875826790815802870,
           '2026-08-14T18:22:26.769281Z',
           NULL,
           NULL,
           2,
           'What were your key take aways from this event?',
           'SHORT_ANSWER',
           FALSE,
           875826790815802867
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802870
       );


-- ============================================================
-- QUESTION 4
-- How satisfied were you with the logistics?
-- MULTIPLE_CHOICE_GRID
-- ============================================================

INSERT INTO form_template.question_templates (
    id, created_at, last_modified_at, description,
    order_index, question, question_type, required, template_id
)
VALUES (
           875826790815802871,
           '2026-08-14T18:22:26.769281Z',
           NULL,
           '1 = Very dissatisfied   5 = Very satisfied',
           3,
           'How satisfied were you with the logistics?',
           'MULTIPLE_CHOICE_GRID',
           TRUE,
           875826790815802867
       );

INSERT INTO form_template.multiple_choice_grid_templates (
    each_row_required,
    question_template_id
)
VALUES (
           FALSE,
           875826790815802871
       );


-- Columns

INSERT INTO form_template.multiple_choice_grid_column_templates (
    id,
    column_name,
    order_index,
    multiple_choice_grid_template_question_template_id
)
VALUES
    (
        875826790815802872,
        '1',
        0,
        875826790815802871
    ),
    (
        875826790815802873,
        '2',
        1,
        875826790815802871
    ),
    (
        875826790815802874,
        '3',
        2,
        875826790815802871
    ),
    (
        875826790815802875,
        '4',
        3,
        875826790815802871
    ),
    (
        875826790815802876,
        '5',
        4,
        875826790815802871
    ),
    (
        875826790815802877,
        'N/A',
        5,
        875826790815802871
    );


-- Rows

INSERT INTO form_template.multiple_choice_grid_row_templates (
    id,
    order_index,
    row_name,
    multiple_choice_grid_template_question_template_id
)
VALUES
    (
        875826790815802878,
        0,
        'Accommodation',
        875826790815802871
    ),
    (
        875826790815802879,
        1,
        'Welcome kit',
        875826790815802871
    ),
    (
        875826790815802880,
        2,
        'Communication emails',
        875826790815802871
    ),
    (
        875826790815802881,
        3,
        'Transportation',
        875826790815802871
    ),
    (
        875826790815802882,
        4,
        'Welcome activity',
        875826790815802871
    ),
    (
        875826790815802883,
        5,
        'Venue',
        875826790815802871
    ),
    (
        875826790815802884,
        6,
        'Activities',
        875826790815802871
    ),
    (
        875826790815802885,
        7,
        'Closing ceremony',
        875826790815802871
    );


-- ============================================================
-- QUESTION 5
-- Additional feedback on logistics
-- SHORT_ANSWER
-- ============================================================

INSERT INTO form_template.question_templates (
    id, created_at, last_modified_at, description,
    order_index, question, question_type, required, template_id
)
VALUES (
           875826790815802886,
           '2026-08-14T18:22:26.769281Z',
           NULL,
           NULL,
           4,
           'Additional feedback on logistics',
           'SHORT_ANSWER',
           TRUE,
           875826790815802867
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802886
       );


-- ============================================================
-- QUESTION 6
-- Which sessions did you find most relevant?
-- MULTIPLE_CHOICE_GRID
-- ============================================================

INSERT INTO form_template.question_templates (
    id, created_at, last_modified_at, description,
    order_index, question, question_type, required, template_id
)
VALUES (
           875826790815802887,
           '2026-08-14T18:22:26.769281Z',
           NULL,
           NULL,
           5,
           'Which sessions did you find most relevant?',
           'MULTIPLE_CHOICE_GRID',
           TRUE,
           875826790815802867
       );

INSERT INTO form_template.multiple_choice_grid_templates (
    each_row_required,
    question_template_id
)
VALUES (
           FALSE,
           875826790815802887
       );


-- Columns

INSERT INTO form_template.multiple_choice_grid_column_templates (
    id,
    column_name,
    order_index,
    multiple_choice_grid_template_question_template_id
)
VALUES
    (
        875826790815802888,
        'Not relevant',
        0,
        875826790815802887
    ),
    (
        875826790815802889,
        'Relevant',
        1,
        875826790815802887
    ),
    (
        875826790815802890,
        'Very relevant',
        2,
        875826790815802887
    ),
    (
        875826790815802891,
        'Did not attend',
        3,
        875826790815802887
    );


-- Rows

INSERT INTO form_template.multiple_choice_grid_row_templates (
    id,
    order_index,
    row_name,
    multiple_choice_grid_template_question_template_id
)
VALUES
    (
        875826790815802892,
        0,
        'Welcome activity',
        875826790815802887
    ),
    (
        875826790815802893,
        1,
        'Speaker #1',
        875826790815802887
    ),
    (
        875826790815802894,
        2,
        'Activity #1',
        875826790815802887
    ),
    (
        875826790815802895,
        3,
        'Speaker #2',
        875826790815802887
    ),
    (
        875826790815802896,
        4,
        'Activity #2',
        875826790815802887
    ),
    (
        875826790815802897,
        5,
        'Closing activity',
        875826790815802887
    );


-- ============================================================
-- QUESTION 7
-- How satisfied were you with the session content?
-- LINEAR_SCALE 1-5
-- ============================================================

INSERT INTO form_template.question_templates (
    id, created_at, last_modified_at, description,
    order_index, question, question_type, required, template_id
)
VALUES (
           875826790815802898,
           '2026-08-14T18:22:26.769281Z',
           NULL,
           'Both presented and pre-read material',
           6,
           'How satisfied were you with the session content?',
           'LINEAR_SCALE',
           FALSE,
           875826790815802867
       );

INSERT INTO form_template.linear_scale_templates (
    from_number,
    to_number,
    question_template_id
)
VALUES (
           1,
           5,
           875826790815802898
       );


-- ============================================================
-- QUESTION 8
-- Any additional comments regarding the sessions or overall agenda?
-- SHORT_ANSWER
-- ============================================================

INSERT INTO form_template.question_templates (
    id, created_at, last_modified_at, description,
    order_index, question, question_type, required, template_id
)
VALUES (
           875826790815802899,
           '2026-08-14T18:22:26.769281Z',
           NULL,
           NULL,
           7,
           'Any additional comments regarding the sessions or overall agenda?',
           'SHORT_ANSWER',
           FALSE,
           875826790815802867
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802899
       );


-- ============================================================
-- QUESTION 9
-- Any overall feedback for the event?
-- SHORT_ANSWER
-- ============================================================

INSERT INTO form_template.question_templates (
    id, created_at, last_modified_at, description,
    order_index, question, question_type, required, template_id
)
VALUES (
           875826790815802900,
           '2026-08-14T18:22:26.769281Z',
           NULL,
           NULL,
           8,
           'Any overall feedback for the event?',
           'SHORT_ANSWER',
           FALSE,
           875826790815802867
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802900
       );


-- ============================================================
-- QUESTION 10
-- Name (optional)
-- SHORT_ANSWER
-- ============================================================

INSERT INTO form_template.question_templates (
    id, created_at, last_modified_at, description,
    order_index, question, question_type, required, template_id
)
VALUES (
           875826790815802901,
           '2026-08-14T18:22:26.769281Z',
           NULL,
           NULL,
           9,
           'Name (optional)',
           'SHORT_ANSWER',
           FALSE,
           875826790815802867
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802901
       );


COMMIT;
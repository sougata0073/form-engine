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
           875826790815802937,
           '2026-08-14T18:39:53.391676Z',
           NULL,
           'In emergencies please contact us at (123) 456-7890 or no_reply@example.com',
           'Work Request',
           'Work Request',
           (
               SELECT id
               FROM form_template.template_categories
               WHERE name = 'Work'
           )
       );


-- ============================================================
-- QUESTION 1
-- Name
-- ============================================================

INSERT INTO form_template.question_templates (
    id,
    created_at,
    last_modified_at,
    description,
    order_index,
    question,
    question_type,
    required,
    template_id
)
VALUES (
           875826790815802938,
           '2026-08-14T18:39:53.391676Z',
           NULL,
           NULL,
           0,
           'Name',
           'SHORT_ANSWER',
           TRUE,
           875826790815802937
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802938
       );


-- ============================================================
-- QUESTION 2
-- Email address
-- ============================================================

INSERT INTO form_template.question_templates (
    id,
    created_at,
    last_modified_at,
    description,
    order_index,
    question,
    question_type,
    required,
    template_id
)
VALUES (
           875826790815802939,
           '2026-08-14T18:39:53.391676Z',
           NULL,
           NULL,
           1,
           'Email address',
           'SHORT_ANSWER',
           TRUE,
           875826790815802937
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802939
       );


-- ============================================================
-- QUESTION 3
-- Summary
-- ============================================================

INSERT INTO form_template.question_templates (
    id,
    created_at,
    last_modified_at,
    description,
    order_index,
    question,
    question_type,
    required,
    template_id
)
VALUES (
           875826790815802940,
           '2026-08-14T18:39:53.391676Z',
           NULL,
           NULL,
           2,
           'Summary',
           'SHORT_ANSWER',
           TRUE,
           875826790815802937
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802940
       );


-- ============================================================
-- QUESTION 4
-- Location of problem
-- ============================================================

INSERT INTO form_template.question_templates (
    id,
    created_at,
    last_modified_at,
    description,
    order_index,
    question,
    question_type,
    required,
    template_id
)
VALUES (
           875826790815802941,
           '2026-08-14T18:39:53.391676Z',
           NULL,
           NULL,
           3,
           'Location of problem',
           'SHORT_ANSWER',
           TRUE,
           875826790815802937
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802941
       );


-- ============================================================
-- QUESTION 5
-- Type
-- MULTIPLE_CHOICE
-- ============================================================

INSERT INTO form_template.question_templates (
    id,
    created_at,
    last_modified_at,
    description,
    order_index,
    question,
    question_type,
    required,
    template_id
)
VALUES (
           875826790815802942,
           '2026-08-14T18:39:53.391676Z',
           NULL,
           NULL,
           4,
           'Type',
           'MULTIPLE_CHOICE',
           FALSE,
           875826790815802937
       );

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           875826790815802942
       );

INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES
    (
        875826790815802943,
        'Plumbing',
        0,
        875826790815802942
    ),
    (
        875826790815802944,
        'Lighting',
        1,
        875826790815802942
    ),
    (
        875826790815802945,
        'Heat/AC',
        2,
        875826790815802942
    ),
    (
        875826790815802946,
        'Pests',
        3,
        875826790815802942
    ),
    (
        875826790815802947,
        'Security',
        4,
        875826790815802942
    ),
    (
        875826790815802948,
        'Noise',
        5,
        875826790815802942
    );


-- ============================================================
-- QUESTION 6
-- Priority
-- LINEAR_SCALE 1-5
-- ============================================================

INSERT INTO form_template.question_templates (
    id,
    created_at,
    last_modified_at,
    description,
    order_index,
    question,
    question_type,
    required,
    template_id
)
VALUES (
           875826790815802949,
           '2026-08-14T18:39:53.391676Z',
           NULL,
           NULL,
           5,
           'Priority',
           'LINEAR_SCALE',
           FALSE,
           875826790815802937
       );

INSERT INTO form_template.linear_scale_templates (
    from_number,
    to_number,
    question_template_id
)
VALUES (
           1,
           5,
           875826790815802949
       );


-- ============================================================
-- QUESTION 7
-- Due date
-- DATE
-- ============================================================

INSERT INTO form_template.question_templates (
    id,
    created_at,
    last_modified_at,
    description,
    order_index,
    question,
    question_type,
    required,
    template_id
)
VALUES (
           875826790815802950,
           '2026-08-14T18:39:53.391676Z',
           NULL,
           NULL,
           6,
           'Due date',
           'DATE',
           FALSE,
           875826790815802937
       );

INSERT INTO form_template.date_templates (
    question_template_id
)
VALUES (
           875826790815802950
       );


-- ============================================================
-- QUESTION 8
-- More details
-- PARAGRAPH
-- ============================================================

INSERT INTO form_template.question_templates (
    id,
    created_at,
    last_modified_at,
    description,
    order_index,
    question,
    question_type,
    required,
    template_id
)
VALUES (
           875826790815802951,
           '2026-08-14T18:39:53.391676Z',
           NULL,
           NULL,
           7,
           'More details',
           'PARAGRAPH',
           FALSE,
           875826790815802937
       );

INSERT INTO form_template.paragraph_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
           875826790815802951
       );


COMMIT;
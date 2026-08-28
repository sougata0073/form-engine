BEGIN;

-- ============================================================
-- PERSONAL CATEGORY
-- 875826790811607954 = Personal
-- ============================================================


-- ============================================================
-- 1. TEMPLATE
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
           875826790815802828,
           '2026-08-14T18:00:19.738040Z',
           NULL,
           'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur quis sem odio. Sed commodo vestibulum leo, sit amet tempus odio consectetur in.',
           'Party Invite',
           'Party Invite',
           875826790811607954
       );


-- ============================================================
-- 2. QUESTION 1
-- What is your name?
-- SHORT_ANSWER
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
           875826790815802829,
           '2026-08-14T18:00:19.738040Z',
           NULL,
           NULL,
           0,
           'What is your name?',
           'SHORT_ANSWER',
           FALSE,
           875826790815802828
       );


INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802829
       );


-- ============================================================
-- 3. QUESTION 2
-- Can you attend?
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
           875826790815802830,
           '2026-08-14T18:00:19.738040Z',
           NULL,
           NULL,
           1,
           'Can you attend?',
           'MULTIPLE_CHOICE',
           TRUE,
           875826790815802828
       );


INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           875826790815802830
       );


-- Options

INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES
    (
        875826790815802831,
        'Yes,  I''ll be there',
        0,
        875826790815802830
    ),
    (
        875826790815802832,
        'Sorry, can''t make it',
        1,
        875826790815802830
    );


-- ============================================================
-- 4. QUESTION 3
-- How many of you are attending?
-- SHORT_ANSWER
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
           875826790815802833,
           '2026-08-14T18:00:19.738040Z',
           NULL,
           NULL,
           2,
           'How many of you are attending?',
           'SHORT_ANSWER',
           FALSE,
           875826790815802828
       );


INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802833
       );


-- ============================================================
-- 5. QUESTION 4
-- What will you be bringing?
-- CHECKBOX
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
           875826790815802834,
           '2026-08-14T18:00:19.738040Z',
           NULL,
           NULL,
           3,
           'What will you be bringing?',
           'CHECKBOX',
           FALSE,
           875826790815802828
       );


INSERT INTO form_template.checkbox_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "CHECKBOX_NONE"}'::jsonb,
           875826790815802834
       );


-- Checkbox options

INSERT INTO form_template.checkbox_option_templates (
    id,
    option,
    order_index,
    checkbox_template_question_template_id
)
VALUES
    (
        875826790815802835,
        'Mains',
        0,
        875826790815802834
    ),
    (
        875826790815802836,
        'Salad',
        1,
        875826790815802834
    ),
    (
        875826790815802837,
        'Dessert',
        2,
        875826790815802834
    ),
    (
        875826790815802838,
        'Drinks',
        3,
        875826790815802834
    ),
    (
        875826790815802839,
        'Sides/Appetizers',
        4,
        875826790815802834
    );


-- ============================================================
-- 6. QUESTION 5
-- Do you have any allergies or dietary restrictions?
-- SHORT_ANSWER
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
           875826790815802840,
           '2026-08-14T18:00:19.738040Z',
           NULL,
           NULL,
           4,
           'Do you have any allergies or dietary restrictions?',
           'SHORT_ANSWER',
           FALSE,
           875826790815802828
       );


INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802840
       );


-- ============================================================
-- 7. QUESTION 6
-- What is your email address?
-- SHORT_ANSWER
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
           875826790815802841,
           '2026-08-14T18:00:19.738040Z',
           NULL,
           NULL,
           5,
           'What is your email address?',
           'SHORT_ANSWER',
           FALSE,
           875826790815802828
       );


INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_TEXT_EMAIL"}'::jsonb,
           875826790815802841
       );


COMMIT;
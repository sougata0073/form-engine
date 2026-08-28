BEGIN;

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
           875826790815802851,
           '2026-08-14T18:14:16.629670Z',
           NULL,
           E'Event Timing: January 4th-6th, 2016\nEvent Address: 123 Your Street Your City, ST 12345\nContact us at (123) 456-7890 or no_reply@example.com',
           'Event registration',
           'Event registration',
           875826790811607954
       );


-- ============================================================
-- 2. QUESTION 1
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
           875826790815802852,
           '2026-08-14T18:14:16.629670Z',
           NULL,
           NULL,
           0,
           'Name',
           'SHORT_ANSWER',
           TRUE,
           875826790815802851
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802852
       );


-- ============================================================
-- 3. QUESTION 2
-- Email
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
           875826790815802853,
           '2026-08-14T18:14:16.629670Z',
           NULL,
           NULL,
           1,
           'Email',
           'SHORT_ANSWER',
           TRUE,
           875826790815802851
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_TEXT_EMAIL"}'::jsonb,
           875826790815802853
       );


-- ============================================================
-- 4. QUESTION 3
-- Organization
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
           875826790815802854,
           '2026-08-14T18:14:16.629670Z',
           NULL,
           NULL,
           2,
           'Organization',
           'SHORT_ANSWER',
           TRUE,
           875826790815802851
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802854
       );


-- ============================================================
-- 5. QUESTION 4
-- What days will you attend?
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
           875826790815802855,
           '2026-08-14T18:14:16.629670Z',
           NULL,
           NULL,
           3,
           'What days will you attend?',
           'CHECKBOX',
           TRUE,
           875826790815802851
       );

INSERT INTO form_template.checkbox_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "CHECKBOX_NONE"}'::jsonb,
           875826790815802855
       );

INSERT INTO form_template.checkbox_option_templates (
    id,
    option,
    order_index,
    checkbox_template_question_template_id
)
VALUES
    (
        875826790815802856,
        'Day 1',
        0,
        875826790815802855
    ),
    (
        875826790815802857,
        'Day 2',
        1,
        875826790815802855
    ),
    (
        875826790815802858,
        'Day 3',
        2,
        875826790815802855
    );


-- ============================================================
-- 6. QUESTION 5
-- Dietary restrictions
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
           875826790815802859,
           '2026-08-14T18:14:16.629670Z',
           NULL,
           NULL,
           4,
           'Dietary restrictions',
           'MULTIPLE_CHOICE',
           TRUE,
           875826790815802851
       );

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           875826790815802859
       );

INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES
    (
        875826790815802860,
        'None',
        0,
        875826790815802859
    ),
    (
        875826790815802861,
        'Vegetarian',
        1,
        875826790815802859
    ),
    (
        875826790815802862,
        'Vegan',
        2,
        875826790815802859
    ),
    (
        875826790815802863,
        'Kosher',
        3,
        875826790815802859
    ),
    (
        875826790815802864,
        'Gluten-free',
        4,
        875826790815802859
    );


-- ============================================================
-- 7. QUESTION 6
-- I understand that I will have to pay $$ upon arrival
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
           875826790815802865,
           '2026-08-14T18:14:16.629670Z',
           NULL,
           NULL,
           5,
           'I understand that I will have to pay $$ upon arrival',
           'CHECKBOX',
           TRUE,
           875826790815802851
       );

INSERT INTO form_template.checkbox_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "CHECKBOX_NONE"}'::jsonb,
           875826790815802865
       );

INSERT INTO form_template.checkbox_option_templates (
    id,
    option,
    order_index,
    checkbox_template_question_template_id
)
VALUES (
           875826790815802866,
           'Yes',
           0,
           875826790815802865
       );


COMMIT;
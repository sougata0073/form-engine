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
           875826790815802902,
           '2026-08-14T18:29:33.953446Z',
           NULL,
           E'After you fill out this order request, we will contact you to go over details and availability before the order is completed. If you would like faster service and direct information on current stock and pricing please contact us at Contact us at (123) 456-7890 or no_reply@example.com',
           'Order Request',
           'Order Request',
           (SELECT id
            FROM form_template.template_categories
            WHERE name = 'Work')
       );


-- ============================================================
-- QUESTION 1
-- Are you a new or existing customer?
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
           875826790815802903,
           '2026-08-14T18:29:33.953446Z',
           NULL,
           NULL,
           0,
           'Are you a new or existing customer?',
           'MULTIPLE_CHOICE',
           FALSE,
           875826790815802902
       );

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           875826790815802903
       );

INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES
    (
        875826790815802904,
        'I am a new customer',
        0,
        875826790815802903
    ),
    (
        875826790815802905,
        'I am an existing customer',
        1,
        875826790815802903
    );


-- ============================================================
-- QUESTION 2
-- What is the item you would like to order?
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
           875826790815802906,
           '2026-08-14T18:29:33.953446Z',
           NULL,
           'Please enter the product number',
           1,
           'What is the item you would like to order?',
           'SHORT_ANSWER',
           TRUE,
           875826790815802902
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802906
       );


-- ============================================================
-- QUESTION 3
-- What color(s) would you like to order?
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
           875826790815802907,
           '2026-08-14T18:29:33.953446Z',
           NULL,
           NULL,
           2,
           'What color(s) would you like to order?',
           'CHECKBOX',
           FALSE,
           875826790815802902
       );

INSERT INTO form_template.checkbox_templates (
    question_template_id,
    validation_config
)
VALUES (
           875826790815802907,
           '{"errorText": null, "validationId": "CHECKBOX_NONE"}'::jsonb
       );

INSERT INTO form_template.checkbox_option_templates (
    id,
    option,
    order_index,
    checkbox_template_question_template_id
)
VALUES
    (
        875826790815802908,
        'color 1',
        0,
        875826790815802907
    ),
    (
        875826790815802909,
        'color 2',
        1,
        875826790815802907
    ),
    (
        875826790815802910,
        'color 3',
        2,
        875826790815802907
    ),
    (
        875826790815802911,
        'color 4',
        3,
        875826790815802907
    );


-- ============================================================
-- QUESTION 4
-- Product options
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
           875826790815802912,
           '2026-08-14T18:29:33.953446Z',
           NULL,
           NULL,
           3,
           'Product options',
           'PARAGRAPH',
           FALSE,
           875826790815802902
       );

INSERT INTO form_template.paragraph_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
           875826790815802912
       );


-- ============================================================
-- QUESTION 5
-- Your name
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
           875826790815802913,
           '2026-08-14T18:29:33.953446Z',
           NULL,
           NULL,
           4,
           'Your name',
           'SHORT_ANSWER',
           TRUE,
           875826790815802902
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802913
       );


-- ============================================================
-- QUESTION 6
-- Phone number
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
           875826790815802914,
           '2026-08-14T18:29:33.953446Z',
           NULL,
           NULL,
           5,
           'Phone number',
           'SHORT_ANSWER',
           TRUE,
           875826790815802902
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802914
       );


-- ============================================================
-- QUESTION 7
-- E-mail
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
           875826790815802915,
           '2026-08-14T18:29:33.953446Z',
           NULL,
           NULL,
           6,
           'E-mail',
           'SHORT_ANSWER',
           FALSE,
           875826790815802902
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_TEXT_EMAIL"}'::jsonb,
           875826790815802915
       );


-- ============================================================
-- QUESTION 8
-- Preferred contact method
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
           875826790815802916,
           '2026-08-14T18:29:33.953446Z',
           NULL,
           NULL,
           7,
           'Preferred contact method',
           'CHECKBOX',
           TRUE,
           875826790815802902
       );

INSERT INTO form_template.checkbox_templates (
    question_template_id,
    validation_config
)
VALUES (
           875826790815802916,
           '{"errorText": null, "validationId": "CHECKBOX_NONE"}'::jsonb
       );

INSERT INTO form_template.checkbox_option_templates (
    id,
    option,
    order_index,
    checkbox_template_question_template_id
)
VALUES
    (
        875826790815802917,
        'Phone',
        0,
        875826790815802916
    ),
    (
        875826790815802918,
        'Email',
        1,
        875826790815802916
    );


-- ============================================================
-- QUESTION 9
-- Questions and comments
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
           875826790815802919,
           '2026-08-14T18:29:33.953446Z',
           NULL,
           NULL,
           8,
           'Questions and comments',
           'PARAGRAPH',
           FALSE,
           875826790815802902
       );

INSERT INTO form_template.paragraph_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
           875826790815802919
       );


COMMIT;